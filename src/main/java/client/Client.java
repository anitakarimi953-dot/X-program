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

            // 1. Register
            Message registerMessage = new Message(
                    "REGISTER",
                    "test|Test1234"
            );

            String registerJson = gson.toJson(registerMessage);

            output.println(registerJson);

            System.out.println(
                    "Register JSON: " + registerJson
            );

            String registerResponse = input.readLine();

            System.out.println(
                    "Register response: " + registerResponse
            );

            // 2. Login
            Message loginMessage = new Message(
                    "LOGIN",
                    "test|Test1234"
            );

            String loginJson = gson.toJson(loginMessage);

            output.println(loginJson);

            System.out.println(
                    "Login JSON: " + loginJson
            );

            String loginResponse = input.readLine();

            System.out.println(
                    "Login response: " + loginResponse
            );

            // 3. دریافت Session
            if (loginResponse != null
                    && loginResponse.startsWith("LOGIN_SUCCESS|")) {

                String sessionId =
                        loginResponse.substring(
                                "LOGIN_SUCCESS|".length()
                        );

                System.out.println(
                        "Session ID: " + sessionId
                );

                // 4. Logout
                Message logoutMessage = new Message(
                        "LOGOUT",
                        sessionId
                );

                String logoutJson = gson.toJson(logoutMessage);

                output.println(logoutJson);

                System.out.println(
                        "Logout JSON: " + logoutJson
                );

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