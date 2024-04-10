package eus.ehu.ridesfx.msgClient;

import eus.ehu.ridesfx.uicontrollers.CarPoolChatController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MsgClient {
    private final String senderEmail;
    private final Socket clientSocket;
    private CarPoolChatController chatController;

    private final BufferedReader in;
    private PrintWriter out;
    final Scanner sc = new Scanner(System.in);

    public MsgClient(String senderEmail) throws IOException {
        this.senderEmail = senderEmail;
        this.clientSocket = new Socket("localhost", 5000);
        this.out = new PrintWriter(this.clientSocket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String msg = sc.nextLine();
                    out.println(msg);
                    out.flush();
                }
            }
        });

        sender.start();

        Thread receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = in.readLine();
                        System.out.println("Server: " + msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        receiver.start();
    }


    public void sendMessage(int ridenumber, String message) {
        out.println(senderEmail + ": " + message);
        out.flush();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public void setChatController(CarPoolChatController chatController) {
        this.chatController = chatController;
    }
}