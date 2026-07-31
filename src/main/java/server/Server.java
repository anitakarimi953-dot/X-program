package server;

import com.google.gson.Gson;
import common.Message;
import common.Tweet;
import common.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int port;

    private final UserManager userManager;
    private final SessionManager sessionManager;
    private final TweetManager tweetManager;
    private final Gson gson;

    public Server(int port) {
        this.port = port;
        this.userManager = new UserManager();
        this.sessionManager = new SessionManager();
        this.tweetManager = new TweetManager();
        this.gson = new Gson();
    }

    public void start() {

        try (ServerSocket serverSocket =
                     new ServerSocket(port)) {

            System.out.println(
                    "Server started on port " + port
            );

            while (true) {

                Socket socket =
                        serverSocket.accept();

                System.out.println(
                        "Client connected!"
                );

                handleClient(socket);
            }

        } catch (IOException e) {

            System.out.println(
                    "Server error: " + e.getMessage()
            );
        }
    }

    private void handleClient(Socket socket) {

        try (
                Socket clientSocket = socket;

                BufferedReader input =
                        new BufferedReader(
                                new InputStreamReader(
                                        clientSocket.getInputStream()
                                )
                        );

                PrintWriter output =
                        new PrintWriter(
                                clientSocket.getOutputStream(),
                                true
                        )
        ) {

            String json = input.readLine();

            if (json == null) {
                return;
            }

            System.out.println(
                    "JSON received: " + json
            );

            Message message =
                    gson.fromJson(
                            json,
                            Message.class
                    );

            if (message == null) {
                output.println("INVALID_REQUEST");
                return;
            }

            switch (message.getType()) {

                case "REGISTER":
                    handleRegister(message, output);
                    break;

                case "LOGIN":
                    handleLogin(message, output);
                    break;

                case "LOGOUT":
                    handleLogout(message, output);
                    break;

                case "CHECK_SESSION":
                    handleCheckSession(message, output);
                    break;

                case "CREATE_TWEET":
                    handleCreateTweet(message, output);
                    break;

                case "GET_TWEETS":
                    handleGetTweets(message, output);
                    break;

                default:
                    output.println("UNKNOWN_COMMAND");
            }

        } catch (Exception e) {

            System.out.println(
                    "Client error: " + e.getMessage()
            );
        }
    }

    private void handleRegister(
            Message message,
            PrintWriter output
    ) {

        String[] parts =
                message.getContent().split("\\|", 2);

        if (parts.length != 2) {
            output.println("REGISTER_FAILED");
            return;
        }

        String username = parts[0];
        String password = parts[1];

        User user =
                new User(username, password);

        boolean registered =
                userManager.register(user);

        output.println(
                registered
                        ? "REGISTER_SUCCESS"
                        : "REGISTER_FAILED"
        );
    }

    private void handleLogin(
            Message message,
            PrintWriter output
    ) {

        String[] parts =
                message.getContent().split("\\|", 2);

        if (parts.length != 2) {
            output.println("LOGIN_FAILED");
            return;
        }

        String username = parts[0];
        String password = parts[1];

        boolean loggedIn =
                userManager.login(
                        username,
                        password
                );

        if (!loggedIn) {
            output.println("LOGIN_FAILED");
            return;
        }

        String sessionId =
                sessionManager.createSession(
                        username
                );

        output.println(
                "LOGIN_SUCCESS|" + sessionId
        );
    }

    private void handleLogout(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (!sessionManager.isValid(sessionId)) {
            output.println("LOGOUT_FAILED");
            return;
        }

        sessionManager.removeSession(sessionId);

        output.println("LOGOUT_SUCCESS");
    }

    private void handleCheckSession(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (!sessionManager.isValid(sessionId)) {
            output.println("SESSION_INVALID");
            return;
        }

        String username =
                sessionManager.getUsername(sessionId);

        output.println(
                "SESSION_VALID|" + username
        );
    }

    private void handleCreateTweet(
            Message message,
            PrintWriter output
    ) {

        String[] parts =
                message.getContent().split("\\|", 2);

        if (parts.length != 2) {
            output.println("TWEET_CREATE_FAILED");
            return;
        }

        String sessionId = parts[0];
        String content = parts[1];

        if (!sessionManager.isValid(sessionId)) {
            output.println("SESSION_INVALID");
            return;
        }

        String username =
                sessionManager.getUsername(sessionId);

        Tweet tweet =
                tweetManager.createTweet(
                        username,
                        content
                );

        if (tweet == null) {
            output.println("TWEET_CREATE_FAILED");
            return;
        }

        output.println(
                "TWEET_CREATE_SUCCESS|"
                        + tweet.getId()
        );
    }

    private void handleGetTweets(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (!sessionManager.isValid(sessionId)) {
            output.println("SESSION_INVALID");
            return;
        }

        String json =
                gson.toJson(
                        tweetManager.getAllTweets()
                );

        output.println(
                "TWEETS|" + json
        );
    }
}