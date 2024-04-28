package eus.ehu.ridesfx.msgClient;

import eus.ehu.ridesfx.uicontrollers.CarPoolChatController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MsgClient {
    private static final String SERVER_ADDRESS = "158.179.210.27";
    private static final int PORT = 25565;
    private final String senderUsername;
    private final Socket clientSocket;
    private static CarPoolChatController chatController;

    private final BufferedReader in;
    private PrintWriter out;
    final Scanner sc = new Scanner(System.in);

    public MsgClient(String senderUsername) throws IOException {
        this.senderUsername = senderUsername;
        this.clientSocket = new Socket(SERVER_ADDRESS, PORT);
        this.out = new PrintWriter(this.clientSocket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread messageReaderThread = new Thread(new MessageReader(this.in, this.senderUsername));
        messageReaderThread.start();

    }

    public CarPoolChatController getChatController() {
        return chatController;
    }

    private static class MessageReader implements Runnable {
        private BufferedReader reader;
        private String senderUsername;
        public MessageReader(BufferedReader reader, String senderUsername) {
            this.reader = reader;
            this.senderUsername = senderUsername;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    String[] parts = message.split(":");
                    if (parts[0].equals(chatController.getRide().getRideNumber() + "") &&
                            (!parts[1].trim().equals(this.senderUsername) && !parts[1].equals("sys"))
                            || parts[0].equals("0")) {
                        System.out.println(message);
                        String finalMessage = parts[2];
                        String sender = parts[1];
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                chatController.addMessage(sender, finalMessage, false, false);
                            }
                        });
                    } else if (parts[0].equals(chatController.getRide().getRideNumber() + "") && parts[1].equals("sys")) {
                        String intent = parts[2];
                        switch (intent) {
                            case "join" -> {
                                chatController.addOnline(parts[3]);
                            }
                            case "leave" -> {
                                chatController.removeOnline(parts[3]);
                            }
                            case "cancel", "reenable" -> {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        chatController.addMessage("sys", parts[3], false, true);
                                    }
                                });
                            }
                        }
                    }

                }
            } catch (IOException ex) {
                System.out.println("Socket closed!");
            }
        }
    }

    public void sendMessage(int ridenumber, String message) {
        out.println(ridenumber + ":" + senderUsername + ":" + message);
        chatController.addMessage(senderUsername, message, true, false);
        out.flush();
    }

    public void joinChat(int ridenumber, boolean join) {
        String message = join ? " has joined the chat" : " has left the chat";
        out.println(ridenumber +":"+ "sys" + ":join:" + senderUsername + message);
        out.flush();
    }



    public void close() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    public void setChatController(CarPoolChatController chatController) {
        MsgClient.chatController = chatController;
    }
    public String getChat(int rideNumber) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://158.179.210.27:8080/chat/" + rideNumber))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonData = response.body();
            if (jsonData == null || jsonData.equals("Chat not found")) {
                System.out.println("Chat not found");
                return null;
            }
            System.out.println(jsonData);
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}