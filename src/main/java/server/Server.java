package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Message;
import common.Tweet;
import common.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class Server {

    private final int port;

    private final UserManager userManager;
    private final SessionManager sessionManager;
    private final TweetManager tweetManager;
    private final Gson gson;

    public Server(int port) {

        this.port = port;

        userManager = new UserManager();
        sessionManager = new SessionManager();
        tweetManager = new TweetManager();

        gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        new LocalDateTimeAdapter()
                )
                .create();
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

                output.println(
                        "INVALID_REQUEST"
                );

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

            e.printStackTrace();
        }
    }

    // =========================================================
    // REGISTER
    // =========================================================

    private void handleRegister(
            Message message,
            PrintWriter output
    ) {

        String content = message.getContent();

        if (content == null) {
            output.println("REGISTER_FAILED");
            return;
        }

        String[] parts =
                content.split("\\|", 2);

        if (parts.length != 2) {
            output.println("REGISTER_FAILED");
            return;
        }

        String username = parts[0].trim();
        String password = parts[1];

        if (username.isEmpty() || password.isEmpty()) {
            output.println("REGISTER_FAILED");
            return;
        }

        User user =
                new User(
                        username,
                        password
                );

        boolean registered =
                userManager.register(user);

        if (registered) {

            System.out.println(
                    "User registered: " + username
            );

            output.println(
                    "REGISTER_SUCCESS"
            );

        } else {

            output.println(
                    "REGISTER_FAILED"
            );
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void handleLogin(
            Message message,
            PrintWriter output
    ) {

        String content = message.getContent();

        if (content == null) {
            output.println("LOGIN_FAILED");
            return;
        }

        String[] parts =
                content.split("\\|", 2);

        if (parts.length != 2) {
            output.println("LOGIN_FAILED");
            return;
        }

        String username = parts[0].trim();
        String password = parts[1];

        boolean loggedIn =
                userManager.login(
                        username,
                        password
                );

        if (!loggedIn) {

            System.out.println(
                    "Login failed: " + username
            );

            output.println(
                    "LOGIN_FAILED"
            );

            return;
        }

        String sessionId =
                sessionManager.createSession(
                        username
                );

        System.out.println(
                "Login successful: " + username
        );

        System.out.println(
                "Session created: " + sessionId
        );

        output.println(
                "LOGIN_SUCCESS|" + sessionId
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (sessionId == null ||
                !sessionManager.isValid(sessionId)) {

            output.println(
                    "LOGOUT_FAILED"
            );

            return;
        }

        sessionManager.removeSession(
                sessionId
        );

        System.out.println(
                "Logout successful"
        );

        output.println(
                "LOGOUT_SUCCESS"
        );
    }

    // =========================================================
    // CHECK SESSION
    // =========================================================

    private void handleCheckSession(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (sessionId == null ||
                !sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        String username =
                sessionManager.getUsername(
                        sessionId
                );

        output.println(
                "SESSION_VALID|" + username
        );
    }

    // =========================================================
    // CREATE TWEET
    // =========================================================

    private void handleCreateTweet(
            Message message,
            PrintWriter output
    ) {

        String content =
                message.getContent();

        if (content == null) {
            output.println(
                    "TWEET_CREATE_FAILED"
            );
            return;
        }

        String[] parts =
                content.split("\\|", 2);

        if (parts.length != 2) {

            output.println(
                    "TWEET_CREATE_FAILED"
            );

            return;
        }

        String sessionId = parts[0];
        String tweetContent = parts[1];

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        if (tweetContent.trim().isEmpty()) {

            output.println(
                    "TWEET_CREATE_FAILED"
            );

            return;
        }

        String username =
                sessionManager.getUsername(
                        sessionId
                );

        Tweet tweet =
                tweetManager.createTweet(
                        username,
                        tweetContent
                );

        if (tweet == null) {

            output.println(
                    "TWEET_CREATE_FAILED"
            );

            return;
        }

        System.out.println(
                "Tweet created: id=" +
                        tweet.getId()
        );

        output.println(
                "TWEET_CREATE_SUCCESS|" +
                        tweet.getId()
        );
    }

    // =========================================================
    // GET TWEETS
    // =========================================================

    private void handleGetTweets(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (sessionId == null ||
                !sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        try {

            List<Tweet> tweets =
                    tweetManager.getAllTweets();

            String json =
                    gson.toJson(tweets);

            output.println(
                    "TWEETS|" + json
            );

        } catch (Exception e) {

            System.out.println(
                    "Tweet serialization error: " +
                            e.getMessage()
            );

            output.println(
                    "TWEETS_FAILED"
            );
        }
    }
}