package client;

import java.io.IOException;
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

            System.out.println("Connected to server!");

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}