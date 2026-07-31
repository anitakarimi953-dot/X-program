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

    public String register(String username, String password) {

        try (Socket socket = new Socket(host, port)) {

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            Gson gson = new Gson();

            Message message = new Message(
                    "REGISTER",
                    username + "|" + password
            );

            output.println(gson.toJson(message));

            return input.readLine();

        } catch (IOException e) {
            return "CONNECTION_ERROR";
        }
    }

    public String login(String username, String password) {

        try (Socket socket = new Socket(host, port)) {

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            Gson gson = new Gson();

            Message message = new Message(
                    "LOGIN",
                    username + "|" + password
            );

            output.println(gson.toJson(message));

            return input.readLine();

        } catch (IOException e) {
            return "CONNECTION_ERROR";
        }
    }

    public String logout(String sessionId) {

        try (Socket socket = new Socket(host, port)) {

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            Gson gson = new Gson();

            Message message = new Message(
                    "LOGOUT",
                    sessionId
            );

            output.println(gson.toJson(message));

            return input.readLine();

        } catch (IOException e) {
            return "CONNECTION_ERROR";
        }
    }

    public String checkSession(String sessionId) {

        try (Socket socket = new Socket(host, port)) {

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            Gson gson = new Gson();

            Message message = new Message(
                    "CHECK_SESSION",
                    sessionId
            );

            output.println(gson.toJson(message));

            return input.readLine();

        } catch (IOException e) {
            return "CONNECTION_ERROR";
        }
    }

    public String createTweet(
            String sessionId,
            String content
    ) {

        try (Socket socket = new Socket(host, port)) {

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            Gson gson = new Gson();

            Message message = new Message(
                    "CREATE_TWEET",
                    sessionId + "|" + content
            );

            output.println(gson.toJson(message));

            return input.readLine();

        } catch (IOException e) {
            return "CONNECTION_ERROR";
        }
    }
}