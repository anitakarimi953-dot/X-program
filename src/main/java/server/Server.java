package server;

import common.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;
    private final UserManager userManager = new UserManager();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            Socket clientSocket = serverSocket.accept();

            System.out.println("Client connected!");

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            PrintWriter output = new PrintWriter(
                    clientSocket.getOutputStream(), true
            );

            String message = input.readLine();

            System.out.println("Message received: " + message);

            if (message != null && message.startsWith("REGISTER|")) {

                String userData = message.substring("REGISTER|".length());
                String[] parts = userData.split("\\|");

                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    User user = new User(username, password);

                    boolean registered = userManager.register(user);

                    if (registered) {
                        System.out.println("User registered successfully!");
                        output.println("REGISTER_SUCCESS");
                    } else {
                        System.out.println("Username already exists!");
                        output.println("REGISTER_FAILED");
                    }
                }

            } else if (message != null && message.startsWith("LOGIN|")) {

                String userData = message.substring("LOGIN|".length());
                String[] parts = userData.split("\\|");

                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    boolean loggedIn = userManager.login(username, password);

                    if (loggedIn) {
                        System.out.println("Login successful!");
                        output.println("LOGIN_SUCCESS");
                    } else {
                        System.out.println("Login failed!");
                        output.println("LOGIN_FAILED");
                    }
                }
            }

            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}