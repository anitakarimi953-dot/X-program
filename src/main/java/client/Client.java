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

            // Login
            Message loginMessage = new Message(
                    "LOGIN",
                    "test|1234"
            );

            String loginJson = gson.toJson(loginMessage);

            output.println(loginJson);

            System.out.println("JSON sent: " + loginJson);

            String response = input.readLine();

            System.out.println("Server response: " + response);

            // دریافت Session ID
            if (response != null && response.startsWith("LOGIN_SUCCESS|")) {

                String sessionId =
                        response.substring("LOGIN_SUCCESS|".length());

                System.out.println("Session ID: " + sessionId);

                // Logout
                Message logoutMessage = new Message(
                        "LOGOUT",
                        sessionId
                );

                String logoutJson = gson.toJson(logoutMessage);

                output.println(logoutJson);

                System.out.println("Logout JSON sent: " + logoutJson);

                String logoutResponse = input.readLine();

                System.out.println(
                        "Logout response: " + logoutResponse
                );
            }

        } catch (IOException e) {
            System.out.println(
                    "Client error: " + e.getMessage()
            );
        }
    }
}