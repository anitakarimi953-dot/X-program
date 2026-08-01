package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.Profile;
import common.Tweet;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final String sessionId;
    private final Client client;

    private JTextArea tweetArea;
    private JTextArea feedArea;

    private JTextField usernameField;

    public DashboardFrame(String sessionId) {

        this.sessionId = sessionId;
        this.client = new Client("localhost", 5000);

        setTitle("X-Program Dashboard");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(10, 10)
        );

        // =====================================================
        // TOP
        // =====================================================

        JPanel topPanel =
                new JPanel(
                        new GridLayout(2, 1)
                );

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome to X-Program!"
                );

        JLabel sessionLabel =
                new JLabel(
                        "Session: " + sessionId
                );

        topPanel.add(welcomeLabel);
        topPanel.add(sessionLabel);

        add(
                topPanel,
                BorderLayout.NORTH
        );

        // =====================================================
        // CENTER
        // =====================================================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        // =====================================================
        // CREATE TWEET
        // =====================================================

        JPanel createPanel =
                new JPanel(
                        new BorderLayout(5, 5)
                );

        JLabel tweetLabel =
                new JLabel(
                        "Write your Tweet:"
                );

        tweetArea =
                new JTextArea(5, 40);

        tweetArea.setLineWrap(true);
        tweetArea.setWrapStyleWord(true);

        JScrollPane tweetScroll =
                new JScrollPane(
                        tweetArea
                );

        JButton postTweetButton =
                new JButton(
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
        // FOLLOW / PROFILE PANEL
        // =====================================================

        JPanel followPanel =
                new JPanel(
                        new BorderLayout(5, 5)
                );

        usernameField =
                new JTextField();

        usernameField.setToolTipText(
                "Enter username"
        );

        JPanel followButtons =
                new JPanel(
                        new FlowLayout()
                );

        JButton followButton =
                new JButton(
                        "Follow"
                );

        JButton unfollowButton =
                new JButton(
                        "Unfollow"
                );

        JButton followingButton =
                new JButton(
                        "Following"
                );

        JButton followersButton =
                new JButton(
                        "Followers"
                );

        JButton profileButton =
                new JButton(
                        "View Profile"
                );

        followButtons.add(
                followButton
        );

        followButtons.add(
                unfollowButton
        );

        followButtons.add(
                followingButton
        );

        followButtons.add(
                followersButton
        );

        followButtons.add(
                profileButton
        );

        followPanel.add(
                usernameField,
                BorderLayout.CENTER
        );

        followPanel.add(
                followButtons,
                BorderLayout.SOUTH
        );

        // =====================================================
        // FEED
        // =====================================================

        JPanel feedPanel =
                new JPanel(
                        new BorderLayout(5, 5)
                );

        JLabel feedLabel =
                new JLabel(
                        "Tweet Feed:"
                );

        feedArea =
                new JTextArea();

        feedArea.setEditable(false);
        feedArea.setLineWrap(true);
        feedArea.setWrapStyleWord(true);

        JScrollPane feedScroll =
                new JScrollPane(
                        feedArea
                );

        JButton refreshButton =
                new JButton(
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

        // =====================================================
        // CENTER LAYOUT
        // =====================================================

        JPanel upperCenter =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        upperCenter.add(
                createPanel,
                BorderLayout.NORTH
        );

        upperCenter.add(
                followPanel,
                BorderLayout.SOUTH
        );

        centerPanel.add(
                upperCenter,
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
        // BOTTOM
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout()
                );

        JButton checkSessionButton =
                new JButton(
                        "Check Session"
                );

        JButton logoutButton =
                new JButton(
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

        followButton.addActionListener(
                e -> followUser()
        );

        unfollowButton.addActionListener(
                e -> unfollowUser()
        );

        followingButton.addActionListener(
                e -> showFollowing()
        );

        followersButton.addActionListener(
                e -> showFollowers()
        );

        profileButton.addActionListener(
                e -> showProfile()
        );

        // =====================================================
        // INITIAL LOAD
        // =====================================================

        loadTweets();
    }

    // =========================================================
    // CREATE TWEET
    // =========================================================

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

            tweetArea.setText("");

            JOptionPane.showMessageDialog(
                    this,
                    "Tweet created successfully."
            );

            loadTweets();

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
                    "Tweet creation failed."
            );
        }
    }

    // =========================================================
    // LOAD TWEETS
    // =========================================================

    private void loadTweets() {

        String response =
                client.getTweets(
                        sessionId
                );

        if (response == null) {

            feedArea.setText(
                    "No response from server."
            );

            return;
        }

        if ("SESSION_INVALID".equals(response)) {

            feedArea.setText(
                    "Session is invalid."
            );

            return;
        }

        if ("CONNECTION_ERROR".equals(response)) {

            feedArea.setText(
                    "Could not connect to server."
            );

            return;
        }

        if (!response.startsWith("TWEETS|")) {

            feedArea.setText(
                    "Unexpected server response:\n"
                            + response
            );

            return;
        }

        String json =
                response.substring(
                        "TWEETS|".length()
                );

        try {

            Gson gson =
                    new Gson();

            java.lang.reflect.Type listType =
                    new TypeToken<List<Tweet>>() {
                    }.getType();

            List<Tweet> tweets =
                    gson.fromJson(
                            json,
                            listType
                    );

            feedArea.setText("");

            if (tweets == null
                    || tweets.isEmpty()) {

                feedArea.setText(
                        "No tweets yet."
                );

                return;
            }

            for (Tweet tweet : tweets) {

                feedArea.append(
                        "@"
                                + tweet.getUsername()
                                + "\n"
                );

                feedArea.append(
                        tweet.getContent()
                                + "\n"
                );

                feedArea.append(
                        "Time: "
                                + tweet.getCreatedAt()
                                + "\n"
                );

                feedArea.append(
                        "Tweet ID: "
                                + tweet.getId()
                                + "\n"
                );

                feedArea.append(
                        "-------------------------\n"
                );
            }

        } catch (Exception e) {

            feedArea.setText(
                    "Could not read tweets:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // FOLLOW USER
    // =========================================================

    private void followUser() {

        String username =
                usernameField.getText().trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a username."
            );

            return;
        }

        String response =
                client.follow(
                        sessionId,
                        username
                );

        if ("FOLLOW_SUCCESS".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "You are now following @"
                            + username
            );

            usernameField.setText("");

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
                    "Could not follow @"
                            + username
            );
        }
    }

    // =========================================================
    // UNFOLLOW USER
    // =========================================================

    private void unfollowUser() {

        String username =
                usernameField.getText().trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a username."
            );

            return;
        }

        String response =
                client.unfollow(
                        sessionId,
                        username
                );

        if ("UNFOLLOW_SUCCESS".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "You unfollowed @"
                            + username
            );

            usernameField.setText("");

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
                    "Could not unfollow @"
                            + username
            );
        }
    }

    // =========================================================
    // SHOW FOLLOWING
    // =========================================================

    private void showFollowing() {

        String response =
                client.getFollowing(
                        sessionId
                );

        if ("SESSION_INVALID".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

            return;
        }

        if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

            return;
        }

        if (response == null
                || !response.startsWith(
                "FOLLOWING|"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected server response:\n"
                            + response
            );

            return;
        }

        String json =
                response.substring(
                        "FOLLOWING|".length()
                );

        try {

            Gson gson =
                    new Gson();

            java.lang.reflect.Type listType =
                    new TypeToken<List<String>>() {
                    }.getType();

            List<String> users =
                    gson.fromJson(
                            json,
                            listType
                    );

            if (users == null
                    || users.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "You are not following anyone."
                );

                return;
            }

            StringBuilder text =
                    new StringBuilder(
                            "Following:\n\n"
                    );

            for (String user : users) {

                text.append("@")
                        .append(user)
                        .append("\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    text.toString()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not read following list."
            );
        }
    }

    // =========================================================
    // SHOW FOLLOWERS
    // =========================================================

    private void showFollowers() {

        String response =
                client.getFollowers(
                        sessionId
                );

        if ("SESSION_INVALID".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

            return;
        }

        if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

            return;
        }

        if (response == null
                || !response.startsWith(
                "FOLLOWERS|"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected server response:\n"
                            + response
            );

            return;
        }

        String json =
                response.substring(
                        "FOLLOWERS|".length()
                );

        try {

            Gson gson =
                    new Gson();

            java.lang.reflect.Type listType =
                    new TypeToken<List<String>>() {
                    }.getType();

            List<String> users =
                    gson.fromJson(
                            json,
                            listType
                    );

            if (users == null
                    || users.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "You have no followers."
                );

                return;
            }

            StringBuilder text =
                    new StringBuilder(
                            "Followers:\n\n"
                    );

            for (String user : users) {

                text.append("@")
                        .append(user)
                        .append("\n");
            }

            JOptionPane.showMessageDialog(
                    this,
                    text.toString()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not read followers list."
            );
        }
    }

    // =========================================================
    // SHOW PROFILE
    // =========================================================

    private void showProfile() {

        String username =
                usernameField.getText().trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter a username."
            );

            return;
        }

        String response =
                client.getProfile(
                        sessionId,
                        username
                );

        if ("SESSION_INVALID".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Session is invalid."
            );

            return;
        }

        if ("CONNECTION_ERROR".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server."
            );

            return;
        }

        if ("USER_NOT_FOUND".equals(response)) {

            JOptionPane.showMessageDialog(
                    this,
                    "User @" + username
                            + " was not found."
            );

            return;
        }

        if (response == null
                || !response.startsWith(
                "PROFILE|"
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unexpected server response:\n"
                            + response
            );

            return;
        }

        String json =
                response.substring(
                        "PROFILE|".length()
                );

        try {

            Gson gson =
                    new Gson();

            Profile profile =
                    gson.fromJson(
                            json,
                            Profile.class
                    );

            if (profile == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Could not read profile."
                );

                return;
            }

            String profileText =
                    "Profile\n\n"
                            + "Username: @"
                            + profile.getUsername()
                            + "\n\n"
                            + "Followers: "
                            + profile
                            .getFollowersCount()
                            + "\n"
                            + "Following: "
                            + profile
                            .getFollowingCount();

            JOptionPane.showMessageDialog(
                    this,
                    profileText,
                    "User Profile",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not read profile:\n"
                            + e.getMessage()
            );
        }
    }

    // =========================================================
    // CHECK SESSION
    // =========================================================

    private void checkSession() {

        String response =
                client.checkSession(
                        sessionId
                );

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

    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        String response =
                client.logout(
                        sessionId
                );

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