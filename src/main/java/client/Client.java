package client;

import com.google.gson.Gson;
import common.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            Gson gson = new Gson();

            Message message = new Message(
                    "LOGIN",
                    "test|1234"
            );

            String jsonMessage = gson.toJson(message);

            output.println(jsonMessage);

            System.out.println("JSON sent: " + jsonMessage);

            String response = input.readLine();

            System.out.println("Server response: " + response);

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}