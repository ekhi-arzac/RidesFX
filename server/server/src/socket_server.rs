use std::net::{TcpListener, TcpStream};
use std::io::{self, Read, Write};
use std::sync::{Arc, Mutex};
use std::thread;
use serde::{Serialize, Deserialize};
use std::fs::{self, OpenOptions};
use std::path::Path;

#[derive(Serialize, Deserialize)]
struct Message {
    sender: String,
    content: String,
}

const PORT: u16 = 25565;

fn main() {
    let listener = TcpListener::bind(("0.0.0.0", PORT)).expect("Failed to bind to port");
    println!("Server started. Listening on port {}", PORT);

    let client_writers = Arc::new(Mutex::new(Vec::<Arc<Mutex<TcpStream>>>::new()));

    for stream in listener.incoming() {
        match stream {
            Ok(client_stream) => {
                let client_writers = Arc::clone(&client_writers);
                thread::spawn(move || {
                    handle_client(client_stream, &client_writers);
                });
            }
            Err(e) => {
                eprintln!("Error accepting connection: {}", e);
            }
        }
    }
}

fn handle_client(mut stream: TcpStream, client_writers: &Arc<Mutex<Vec<Arc<Mutex<TcpStream>>>>>) {
    println!("New client connected: {:?}", stream);

    let client_writer = Arc::new(Mutex::new(stream.try_clone().expect("Failed to clone client stream")));
    client_writers.lock().unwrap().push(Arc::clone(&client_writer));

    let mut buffer = [0; 1024];
    loop {
        match stream.read(&mut buffer) {
            Ok(0) => {
                println!("Client disconnected");
                let addr = stream.peer_addr().unwrap();
                client_writers.lock().unwrap().retain(|writer| {
                    let writer_addr = writer.lock().unwrap().peer_addr().unwrap();
                    writer_addr != addr
                });
                break;
            }
            Ok(bytes_read) => {
                let message = std::str::from_utf8(&buffer[..bytes_read]).expect("Failed to parse message").to_string();
                broadcast_message(&message, client_writers);

                let _ = save_message_to_file(&message);
            }
            Err(ref e) if e.kind() == io::ErrorKind::ConnectionReset => {
                println!("Client connection reset: {:?}", e);
                let addr = stream.peer_addr().unwrap();
                client_writers.lock().unwrap().retain(|writer| {
                    let writer_addr = writer.lock().unwrap().peer_addr().unwrap();
                    writer_addr != addr
                });
                break;
            }
            Err(e) => {
                eprintln!("Error reading from client: {}", e);
                break;
            }
        }
    }
}

fn broadcast_message(message: &str, client_writers: &Arc<Mutex<Vec<Arc<Mutex<TcpStream>>>>>) {
    let mut writers = client_writers.lock().unwrap();
    for writer_mutex in writers.iter_mut() {
        let mut writer = writer_mutex.lock().unwrap();
        writer.write_all(message.as_bytes()).expect("Failed to broadcast message");
        writer.write_all(b"\n").expect("Failed to broadcast message");
    }
}

fn save_message_to_file(message: &str) -> io::Result<()> {
    let parsed_msg: Vec<&str> = message.split(':').collect();
    let mut msg_index: usize = 2;
    if parsed_msg.len() > 1 {
        if parsed_msg[1] == "sys" {
            msg_index += 1;
            if parsed_msg[2] != "msg" {
                return Ok(());
            }
        }

        let filename = parsed_msg[0].to_string() + ".json";

        let chats_folder = "../chats";
        if !Path::new(chats_folder).exists() {
            fs::create_dir(chats_folder)?;
        }

        let file_path = Path::new(chats_folder).join(&filename);

        let new_message = Message {
            sender: parsed_msg[1].to_string(),
            content: parsed_msg[msg_index].trim().to_string(),
        };

        let json_contents = if file_path.exists() {
            let existing_contents = fs::read_to_string(&file_path)?;
            let mut messages: Vec<Message> = serde_json::from_str(&existing_contents)?;
            messages.push(new_message);
            serde_json::to_string_pretty(&messages)?
        } else {
            serde_json::to_string_pretty(&vec![new_message])?
        };

        let mut file = OpenOptions::new()
            .write(true)
            .create(true)
            .truncate(true)
            .open(&file_path)?;

        writeln!(file, "{}", json_contents)?;
    } else {
        return Err(io::Error::new(
            io::ErrorKind::InvalidInput,
            "Invalid message format",
        ));
    }
    Ok(())
}
