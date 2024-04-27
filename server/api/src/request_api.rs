use actix_web::{get, web, App, HttpResponse, HttpServer, Responder};
use serde::{Deserialize, Serialize};
use std::fs;

#[derive(Serialize, Deserialize)]
struct Message {
    sender: String,
    content: String,
}

#[derive(Deserialize)]
struct ChatRequest {
    chat_number: u32,
}

#[get("/chat/{chat_number}")]
async fn get_chat(req: web::Path<ChatRequest>) -> impl Responder {
        let chat_number = req.chat_number;
        let file_path = format!("../chats/{}.json", chat_number);
    
        // Read the contents of the JSON file
        match fs::read_to_string(file_path) {
            Ok(contents) => {
                // Deserialize JSON string into a vector of Message structs
                match serde_json::from_str::<Vec<Message>>(&contents) {
                    Ok(messages) => HttpResponse::Ok().json(messages),
                    Err(_) => HttpResponse::InternalServerError().body("Failed to parse chat data"),
                }
            }
            Err(_) => HttpResponse::NotFound().body("Chat not found"),
        }
    }
    
    #[actix_web::main]
    async fn main() -> std::io::Result<()> {
        HttpServer::new(|| {
            App::new()
                .service(get_chat)
        })
        .bind("0.0.0.0:8080")?
        .run()
        .await
    }
    