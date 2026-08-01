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

    private final Gson gson;

    public Client(
            String host,
            int port
    ) {

        this.host = host;
        this.port = port;

        this.gson = new Gson();
    }

    // =========================================================
    // REGISTER
    // =========================================================

    public String register(
            String username,
            String password
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "REGISTER",
                            username + "|" + password
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public String login(
            String username,
            String password
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "LOGIN",
                            username + "|" + password
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    public String logout(
            String sessionId
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "LOGOUT",
                            sessionId
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // CHECK SESSION
    // =========================================================

    public String checkSession(
            String sessionId
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "CHECK_SESSION",
                            sessionId
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // CREATE TWEET
    // =========================================================

    public String createTweet(
            String sessionId,
            String content
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "CREATE_TWEET",
                            sessionId + "|" + content
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // GET TWEETS
    // =========================================================

    public String getTweets(
            String sessionId
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "GET_TWEETS",
                            sessionId
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // FOLLOW
    // =========================================================

    public String follow(
            String sessionId,
            String username
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "FOLLOW",
                            sessionId + "|" + username
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // UNFOLLOW
    // =========================================================

    public String unfollow(
            String sessionId,
            String username
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "UNFOLLOW",
                            sessionId + "|" + username
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // GET FOLLOWING
    // =========================================================

    public String getFollowing(
            String sessionId
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "GET_FOLLOWING",
                            sessionId
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }

    // =========================================================
    // GET FOLLOWERS
    // =========================================================

    public String getFollowers(
            String sessionId
    ) {

        try (Socket socket =
                     new Socket(host, port);

             PrintWriter output =
                     new PrintWriter(
                             socket.getOutputStream(),
                             true
                     );

             BufferedReader input =
                     new BufferedReader(
                             new InputStreamReader(
                                     socket.getInputStream()
                             )
                     )) {

            Message message =
                    new Message(
                            "GET_FOLLOWERS",
                            sessionId
                    );

            output.println(
                    gson.toJson(message)
            );

            return input.readLine();

        } catch (IOException e) {

            return "CONNECTION_ERROR";
        }
    }
}