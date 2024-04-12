package eus.ehu.ridesfx.msgClient;

import eus.ehu.ridesfx.uicontrollers.CarPoolChatController;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
                            !parts[1].trim().equals(this.senderUsername)
                            || parts[0].equals("0")) {
                        System.out.println(message);
                        String finalMessage = parts[2];
                        String sender = parts[1];
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                chatController.addMessage(sender, finalMessage, false);
                            }
                        });
                    }
                }
            } catch (IOException ex) {
                System.out.println("Socket closed!");
            }
        }
    }

    public void sendMessage(int ridenumber, String message) {
        out.println(ridenumber + ":" + senderUsername + ":" + message);
        chatController.addMessage(senderUsername, message, true);
        out.flush();
    }



    public void close() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    public void setChatController(CarPoolChatController chatController) {
        this.chatController = chatController;
    }

}