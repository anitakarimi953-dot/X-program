package server;

import com.google.gson.Gson;
import common.Message;
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
    private final SessionManager sessionManager = new SessionManager();

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

            Gson gson = new Gson();

            String jsonMessage;

            while ((jsonMessage = input.readLine()) != null) {

                System.out.println("JSON received: " + jsonMessage);

                Message message = gson.fromJson(
                        jsonMessage,
                        Message.class
                );

                if (message.getType().equals("REGISTER")) {

                    handleRegister(message, output);

                } else if (message.getType().equals("LOGIN")) {

                    handleLogin(message, output);

                } else if (message.getType().equals("LOGOUT")) {

                    handleLogout(message, output);

                } else {

                    output.println("UNKNOWN_COMMAND");
                }
            }

            clientSocket.close();

        } catch (IOException e) {

            System.out.println(
                    "Server error: " + e.getMessage()
            );
        }
    }

    private void handleRegister(
            Message message,
            PrintWriter output
    ) {

        String[] parts = message.getContent().split("\\|");

        if (parts.length != 2) {
            output.println("REGISTER_FAILED");
            return;
        }

        String username = parts[0];
        String password = parts[1];

        User user = new User(username, password);

        boolean registered = userManager.register(user);

        if (registered) {

            output.println("REGISTER_SUCCESS");

            System.out.println(
                    "User registered: " + username
            );

        } else {

            output.println("REGISTER_FAILED");

            System.out.println(
                    "Username already exists: " + username
            );
        }
    }

    private void handleLogin(
            Message message,
            PrintWriter output
    ) {

        String[] parts = message.getContent().split("\\|");

        if (parts.length != 2) {
            output.println("LOGIN_FAILED");
            return;
        }

        String username = parts[0];
        String password = parts[1];

        boolean loggedIn =
                userManager.login(username, password);

        if (loggedIn) {

            String sessionId =
                    sessionManager.createSession(username);

            output.println(
                    "LOGIN_SUCCESS|" + sessionId
            );

            System.out.println(
                    "Login successful: " + username
            );

            System.out.println(
                    "Session created: " + sessionId
            );

        } else {

            output.println("LOGIN_FAILED");

            System.out.println(
                    "Login failed: " + username
            );
        }
    }

    private void handleLogout(
            Message message,
            PrintWriter output
    ) {

        String sessionId = message.getContent();

        if (sessionManager.isValid(sessionId)) {

            String username =
                    sessionManager.getUsername(sessionId);

            sessionManager.removeSession(sessionId);

            output.println("LOGOUT_SUCCESS");

            System.out.println(
                    "User logged out: " + username
            );

        } else {

            output.println("LOGOUT_FAILED");

            System.out.println("Invalid session!");
        }
    }
}