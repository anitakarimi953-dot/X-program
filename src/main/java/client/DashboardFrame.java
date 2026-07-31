package client;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final String sessionId;
    private final Client client;

    private JTextArea tweetArea;

    public DashboardFrame(String sessionId) {

        this.sessionId = sessionId;
        this.client = new Client("localhost", 5000);

        setTitle("X-Program Dashboard");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // ---------- TOP ----------
        JPanel topPanel = new JPanel(
                new GridLayout(2, 1)
        );

        JLabel welcomeLabel =
                new JLabel("Welcome to X-Program!");

        JLabel sessionLabel =
                new JLabel("Session: " + sessionId);

        topPanel.add(welcomeLabel);
        topPanel.add(sessionLabel);

        add(topPanel, BorderLayout.NORTH);

        // ---------- CENTER ----------
        JPanel tweetPanel = new JPanel(
                new BorderLayout(5, 5)
        );

        JLabel tweetLabel =
                new JLabel("Write your Tweet:");

        tweetArea = new JTextArea(8, 35);
        tweetArea.setLineWrap(true);
        tweetArea.setWrapStyleWord(true);

        JScrollPane scrollPane =
                new JScrollPane(tweetArea);

        JButton postTweetButton =
                new JButton("Post Tweet");

        tweetPanel.add(
                tweetLabel,
                BorderLayout.NORTH
        );

        tweetPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        tweetPanel.add(
                postTweetButton,
                BorderLayout.SOUTH
        );

        add(
                tweetPanel,
                BorderLayout.CENTER
        );

        // ---------- BOTTOM ----------
        JPanel bottomPanel = new JPanel(
                new FlowLayout()
        );

        JButton checkSessionButton =
                new JButton("Check Session");

        JButton logoutButton =
                new JButton("Logout");

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

        // ---------- ACTIONS ----------

        postTweetButton.addActionListener(
                e -> createTweet()
        );

        checkSessionButton.addActionListener(
                e -> checkSession()
        );

        logoutButton.addActionListener(
                e -> logout()
        );
    }

    private void createTweet() {

        String content =
                tweetArea.getText().trim();

        if (content.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tweet cannot be empty."
            );

            return;
        }

        String response =
                client.createTweet(
                        sessionId,
                        content
                );

        if (response != null
                && response.startsWith(
                "TWEET_CREATE_SUCCESS|"
        )) {

            String tweetId =
                    response.substring(
                            "TWEET_CREATE_SUCCESS|"
                                    .length()
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Tweet created successfully!\n"
                            + "Tweet ID: "
                            + tweetId
            );

            tweetArea.setText("");

        } else if (
                "SESSION_INVALID".equals(response)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

        } else if (
                "CONNECTION_ERROR".equals(response)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Tweet creation failed.\n"
                            + response
            );
        }
    }

    private void checkSession() {

        String response =
                client.checkSession(sessionId);

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

        } else if (
                "SESSION_INVALID".equals(response)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

        } else if (
                "CONNECTION_ERROR".equals(response)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected server response:\n"
                            + response
            );
        }
    }

    private void logout() {

        String response =
                client.logout(sessionId);

        if ("LOGOUT_SUCCESS".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Logout successful."
            );

            dispose();

        } else if (
                "CONNECTION_ERROR".equals(response)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Logout failed."
            );
        }
    }
}