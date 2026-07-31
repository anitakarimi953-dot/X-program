package client;

import common.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port)) {

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true
            );

            Message message = new Message(
                    "REGISTER",
                    "test|1234"
            );

            output.println(message);

            System.out.println("Register request sent: " + message);

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}