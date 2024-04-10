package eus.ehu.ridesfx.msgClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class msgServer {
    public static void main(String[] args) {
        final ServerSocket serverSocket;
        final Socket clientSocket;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);

        try {
            serverSocket = new ServerSocket(5000);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
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
                            System.out.println("Client: " + msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            receiver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}