package client;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.Tweet;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class DashboardFrame extends JFrame {

    private final String sessionId;
    private final Client client;

    private final JTextArea tweetArea;
    private final JTextArea feedArea;

    private final Gson gson;

    public DashboardFrame(String sessionId) {

        this.sessionId = sessionId;
        this.client = new Client("localhost", 5000);

        /*
         * مهم:
         * این Gson مخصوص کلاینت است.
         * برای نمایش createdAt لازم نیست Gson مستقیماً
         * LocalDateTime را به Tweet تبدیل کند؛
         * JSON را پایین‌تر دستی می‌خوانیم.
         */
        this.gson = new Gson();

        setTitle("X-Program Dashboard");
        setSize(650, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // =====================================================
        // TOP PANEL
        // =====================================================

        JPanel topPanel = new JPanel(
                new GridLayout(2, 1)
        );

        JLabel welcomeLabel = new JLabel(
                "Welcome to X-Program!"
        );

        JLabel sessionLabel = new JLabel(
                "Session: " + sessionId
        );

        topPanel.add(welcomeLabel);
        topPanel.add(sessionLabel);

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER PANEL
        // =====================================================

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        // =====================================================
        // CREATE TWEET PANEL
        // =====================================================

        JPanel createPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        JLabel tweetLabel = new JLabel(
                "Write your Tweet:"
        );

        tweetArea = new JTextArea(
                5,
                40
        );

        tweetArea.setLineWrap(true);
        tweetArea.setWrapStyleWord(true);

        JScrollPane tweetScroll = new JScrollPane(
                tweetArea
        );

        JButton postTweetButton = new JButton(
                "Post Tweet"
        );

        createPanel.add(
                tweetLabel,
                BorderLayout.NORTH
        );

        createPanel.add(
                tweetScroll,
                BorderLayout.CENTER
        );

        createPanel.add(
                postTweetButton,
                BorderLayout.SOUTH
        );

        // =====================================================
        // FEED PANEL
        // =====================================================

        JPanel feedPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        JLabel feedLabel = new JLabel(
                "Tweet Feed:"
        );

        feedArea = new JTextArea();

        feedArea.setEditable(false);
        feedArea.setLineWrap(true);
        feedArea.setWrapStyleWord(true);

        JScrollPane feedScroll = new JScrollPane(
                feedArea
        );

        JButton refreshButton = new JButton(
                "Refresh Feed"
        );

        feedPanel.add(
                feedLabel,
                BorderLayout.NORTH
        );

        feedPanel.add(
                feedScroll,
                BorderLayout.CENTER
        );

        feedPanel.add(
                refreshButton,
                BorderLayout.SOUTH
        );

        centerPanel.add(
                createPanel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                feedPanel,
                BorderLayout.CENTER
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // BOTTOM PANEL
        // =====================================================

        JPanel bottomPanel = new JPanel(
                new FlowLayout()
        );

        JButton checkSessionButton = new JButton(
                "Check Session"
        );

        JButton logoutButton = new JButton(
                "Logout"
        );

        bottomPanel.add(
                checkSessionButton
        );

        bottomPanel.add(
                logoutButton
        );

        add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        postTweetButton.addActionListener(
                e -> createTweet()
        );

        refreshButton.addActionListener(
                e -> loadTweets()
        );

        checkSessionButton.addActionListener(
                e -> checkSession()
        );

        logoutButton.addActionListener(
                e -> logout()
        );

        // =====================================================
        // LOAD FEED WHEN DASHBOARD OPENS
        // =====================================================

        SwingUtilities.invokeLater(
                this::loadTweets
        );
    }

    // =========================================================
    // CREATE TWEET
    // =========================================================

    private void createTweet() {

        String content = tweetArea
                .getText()
                .trim();

        if (content.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tweet cannot be empty."
            );

            return;
        }

        String response;

        try {

            response = client.createTweet(
                    sessionId,
                    content
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Connection error:\n"
                            + e.getMessage()
            );

            return;
        }

        // -----------------------------------------------------
        // SUCCESS
        // -----------------------------------------------------

        if (response != null
                && response.startsWith(
                "TWEET_CREATE_SUCCESS|"
        )) {

            tweetArea.setText("");

            JOptionPane.showMessageDialog(
                    this,
                    "Tweet created successfully."
            );

            loadTweets();

            return;
        }

        // -----------------------------------------------------
        // INVALID SESSION
        // -----------------------------------------------------

        if ("SESSION_INVALID".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

            return;
        }

        // -----------------------------------------------------
        // CONNECTION ERROR
        // -----------------------------------------------------

        if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

            return;
        }

        // -----------------------------------------------------
        // OTHER ERROR
        // -----------------------------------------------------

        JOptionPane.showMessageDialog(
                this,
                "Tweet creation failed.\n"
                        + "Server response: "
                        + response
        );
    }

    // =========================================================
    // LOAD TWEETS
    // =========================================================

    private void loadTweets() {

        feedArea.setText(
                "Loading tweets..."
        );

        String response;

        try {

            response = client.getTweets(
                    sessionId
            );

        } catch (Exception e) {

            feedArea.setText(
                    "Could not connect to server.\n"
                            + e.getMessage()
            );

            return;
        }

        // -----------------------------------------------------
        // NULL RESPONSE
        // -----------------------------------------------------

        if (response == null) {

            feedArea.setText(
                    "No response from server."
            );

            return;
        }

        // -----------------------------------------------------
        // INVALID SESSION
        // -----------------------------------------------------

        if ("SESSION_INVALID".equals(response)) {

            feedArea.setText(
                    "Session is invalid."
            );

            return;
        }

        // -----------------------------------------------------
        // CONNECTION ERROR
        // -----------------------------------------------------

        if ("CONNECTION_ERROR".equals(response)) {

            feedArea.setText(
                    "Could not connect to server."
            );

            return;
        }

        // -----------------------------------------------------
        // SERVER MUST RETURN TWEETS|JSON
        // -----------------------------------------------------

        if (!response.startsWith("TWEETS|")) {

            feedArea.setText(
                    "Unexpected server response:\n"
                            + response
            );

            return;
        }

        String json = response.substring(
                "TWEETS|".length()
        );

        // -----------------------------------------------------
        // DEBUG
        // -----------------------------------------------------

        System.out.println(
                "CLIENT TWEETS JSON = " + json
        );

        try {

            JsonElement root =
                    JsonParser.parseString(json);

            if (!root.isJsonArray()) {

                feedArea.setText(
                        "Server returned invalid tweet data."
                );

                return;
            }

            JsonArray tweets =
                    root.getAsJsonArray();

            feedArea.setText("");

            // -------------------------------------------------
            // NO TWEETS
            // -------------------------------------------------

            if (tweets.size() == 0) {

                feedArea.setText(
                        "No tweets yet."
                );

                return;
            }

            // -------------------------------------------------
            // READ EVERY TWEET
            // -------------------------------------------------

            for (JsonElement element : tweets) {

                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject tweetObject =
                        element.getAsJsonObject();

                // ---------------------------------------------
                // ID
                // ---------------------------------------------

                String id = "";

                if (tweetObject.has("id")
                        && !tweetObject.get("id").isJsonNull()) {

                    id = tweetObject
                            .get("id")
                            .getAsString();
                }

                // ---------------------------------------------
                // USERNAME
                // ---------------------------------------------

                String username = "";

                if (tweetObject.has("username")
                        && !tweetObject.get("username").isJsonNull()) {

                    username = tweetObject
                            .get("username")
                            .getAsString();
                }

                // ---------------------------------------------
                // CONTENT
                // ---------------------------------------------

                String content = "";

                if (tweetObject.has("content")
                        && !tweetObject.get("content").isJsonNull()) {

                    content = tweetObject
                            .get("content")
                            .getAsString();
                }

                // ---------------------------------------------
                // CREATED AT
                // ---------------------------------------------

                String createdAt = "";

                if (tweetObject.has("createdAt")
                        && !tweetObject.get("createdAt").isJsonNull()) {

                    createdAt = tweetObject
                            .get("createdAt")
                            .getAsString();
                }

                // ---------------------------------------------
                // DISPLAY
                // ---------------------------------------------

                feedArea.append(
                        "@" + username + "\n"
                );

                feedArea.append(
                        content + "\n"
                );

                feedArea.append(
                        "Time: " + createdAt + "\n"
                );

                feedArea.append(
                        "Tweet ID: " + id + "\n"
                );

                feedArea.append(
                        "-------------------------\n"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            feedArea.setText(
                    "Could not read tweets:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK SESSION
    // =========================================================

    private void checkSession() {

        String response;

        try {

            response = client.checkSession(
                    sessionId
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Connection error:\n"
                            + e.getMessage()
            );

            return;
        }

        // -----------------------------------------------------
        // VALID
        // -----------------------------------------------------

        if (response != null
                && response.startsWith(
                "SESSION_VALID|"
        )) {

            String username =
                    response.substring(
                            "SESSION_VALID|".length()
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Session is valid.\n"
                            + "Username: "
                            + username
            );

            return;
        }

        // -----------------------------------------------------
        // INVALID
        // -----------------------------------------------------

        if ("SESSION_INVALID".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

            return;
        }

        // -----------------------------------------------------
        // CONNECTION ERROR
        // -----------------------------------------------------

        if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

            return;
        }

        // -----------------------------------------------------
        // OTHER
        // -----------------------------------------------------

        JOptionPane.showMessageDialog(
                this,
                "Unexpected server response:\n"
                        + response
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        String response;

        try {

            response = client.logout(
                    sessionId
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Connection error:\n"
                            + e.getMessage()
            );

            return;
        }

        if ("LOGOUT_SUCCESS".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Logout successful."
            );

            dispose();

            return;
        }

        if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Logout failed.\n"
                        + "Server response: "
                        + response
        );
    }
}