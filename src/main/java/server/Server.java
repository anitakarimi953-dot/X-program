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
    private final FollowManager followManager;
    private final Gson gson;

    public Server(int port) {

        this.port = port;

        this.userManager = new UserManager();
        this.sessionManager = new SessionManager();
        this.tweetManager = new TweetManager();
        this.followManager = new FollowManager();

        this.gson = new GsonBuilder()
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

                case "FOLLOW":
                    handleFollow(message, output);
                    break;

                case "UNFOLLOW":
                    handleUnfollow(message, output);
                    break;

                case "GET_FOLLOWING":
                    handleGetFollowing(message, output);
                    break;

                case "GET_FOLLOWERS":
                    handleGetFollowers(message, output);
                    break;

                default:
                    output.println(
                            "UNKNOWN_COMMAND"
                    );
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

        String[] parts =
                message.getContent()
                        .split("\\|", 2);

        if (parts.length != 2) {

            output.println(
                    "REGISTER_FAILED"
            );

            return;
        }

        String username = parts[0];
        String password = parts[1];

        User user =
                new User(
                        username,
                        password
                );

        boolean registered =
                userManager.register(user);

        output.println(
                registered
                        ? "REGISTER_SUCCESS"
                        : "REGISTER_FAILED"
        );
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void handleLogin(
            Message message,
            PrintWriter output
    ) {

        String[] parts =
                message.getContent()
                        .split("\\|", 2);

        if (parts.length != 2) {

            output.println(
                    "LOGIN_FAILED"
            );

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

            output.println(
                    "LOGIN_FAILED"
            );

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

    // =========================================================
    // LOGOUT
    // =========================================================

    private void handleLogout(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "LOGOUT_FAILED"
            );

            return;
        }

        sessionManager.removeSession(
                sessionId
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

        if (!sessionManager.isValid(sessionId)) {

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

        String[] parts =
                message.getContent()
                        .split("\\|", 2);

        if (parts.length != 2) {

            output.println(
                    "TWEET_CREATE_FAILED"
            );

            return;
        }

        String sessionId = parts[0];
        String content = parts[1];

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
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
                        content
                );

        if (tweet == null) {

            output.println(
                    "TWEET_CREATE_FAILED"
            );

            return;
        }

        output.println(
                "TWEET_CREATE_SUCCESS|"
                        + tweet.getId()
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

        if (!sessionManager.isValid(sessionId)) {

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
                    "GET_TWEETS error: "
                            + e.getMessage()
            );

            output.println(
                    "TWEETS_FAILED"
            );
        }
    }

    // =========================================================
    // FOLLOW
    // =========================================================

    private void handleFollow(
            Message message,
            PrintWriter output
    ) {

        String[] parts =
                message.getContent()
                        .split("\\|", 2);

        if (parts.length != 2) {

            output.println(
                    "FOLLOW_FAILED"
            );

            return;
        }

        String sessionId = parts[0];
        String targetUsername = parts[1];

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        String username =
                sessionManager.getUsername(
                        sessionId
                );

        boolean success =
                followManager.follow(
                        username,
                        targetUsername
                );

        if (success) {

            output.println(
                    "FOLLOW_SUCCESS"
            );

        } else {

            output.println(
                    "FOLLOW_FAILED"
            );
        }
    }

    // =========================================================
    // UNFOLLOW
    // =========================================================

    private void handleUnfollow(
            Message message,
            PrintWriter output
    ) {

        String[] parts =
                message.getContent()
                        .split("\\|", 2);

        if (parts.length != 2) {

            output.println(
                    "UNFOLLOW_FAILED"
            );

            return;
        }

        String sessionId = parts[0];
        String targetUsername = parts[1];

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        String username =
                sessionManager.getUsername(
                        sessionId
                );

        boolean success =
                followManager.unfollow(
                        username,
                        targetUsername
                );

        if (success) {

            output.println(
                    "UNFOLLOW_SUCCESS"
            );

        } else {

            output.println(
                    "UNFOLLOW_FAILED"
            );
        }
    }

    // =========================================================
    // GET FOLLOWING
    // =========================================================

    private void handleGetFollowing(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        String username =
                sessionManager.getUsername(
                        sessionId
                );

        List<String> users =
                followManager.getFollowing(
                        username
                );

        String json =
                gson.toJson(users);

        output.println(
                "FOLLOWING|" + json
        );
    }

    // =========================================================
    // GET FOLLOWERS
    // =========================================================

    private void handleGetFollowers(
            Message message,
            PrintWriter output
    ) {

        String sessionId =
                message.getContent();

        if (!sessionManager.isValid(sessionId)) {

            output.println(
                    "SESSION_INVALID"
            );

            return;
        }

        String username =
                sessionManager.getUsername(
                        sessionId
                );

        List<String> users =
                followManager.getFollowers(
                        username
                );

        String json =
                gson.toJson(users);

        output.println(
                "FOLLOWERS|" + json
        );
    }
}