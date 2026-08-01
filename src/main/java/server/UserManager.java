package server;

import common.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<User> users = new ArrayList<>();

    public boolean register(User user) {

        if (user == null) {
            return false;
        }

        if (user.getUsername() == null
                || user.getUsername().trim().isEmpty()) {
            return false;
        }

        for (User existingUser : users) {

            if (existingUser.getUsername()
                    .equals(user.getUsername())) {

                return false;
            }
        }

        String hashedPassword =
                PasswordHasher.hash(user.getPassword());

        User hashedUser =
                new User(
                        user.getUsername(),
                        hashedPassword
                );

        users.add(hashedUser);

        return true;
    }

    public boolean login(
            String username,
            String password
    ) {

        if (username == null || password == null) {
            return false;
        }

        String hashedPassword =
                PasswordHasher.hash(password);

        for (User user : users) {

            if (user.getUsername()
                    .equals(username)
                    &&
                    user.getPassword()
                            .equals(hashedPassword)) {

                return true;
            }
        }

        return false;
    }

    public User findUser(String username) {

        if (username == null) {
            return null;
        }

        for (User user : users) {

            if (user.getUsername()
                    .equals(username)) {

                return user;
            }
        }

        return null;
    }

    public boolean follow(
            String username,
            String targetUsername
    ) {

        if (username == null
                || targetUsername == null
                || username.equals(targetUsername)) {

            return false;
        }

        User user = findUser(username);
        User target = findUser(targetUsername);

        if (user == null || target == null) {
            return false;
        }

        return user.getFollowing()
                .add(targetUsername);
    }

    public boolean unfollow(
            String username,
            String targetUsername
    ) {

        User user = findUser(username);

        if (user == null) {
            return false;
        }

        return user.getFollowing()
                .remove(targetUsername);
    }

    public List<String> getFollowing(
            String username
    ) {

        User user = findUser(username);

        if (user == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                user.getFollowing()
        );
    }

    public List<String> getFollowers(
            String username
    ) {

        List<String> followers =
                new ArrayList<>();

        if (findUser(username) == null) {
            return followers;
        }

        for (User user : users) {

            if (user.getFollowing()
                    .contains(username)) {

                followers.add(
                        user.getUsername()
                );
            }
        }

        return followers;
    }

    public int getFollowingCount(
            String username
    ) {

        User user = findUser(username);

        if (user == null) {
            return 0;
        }

        return user.getFollowing().size();
    }

    public int getFollowersCount(
            String username
    ) {

        return getFollowers(username).size();
    }

    public List<User> getUsers() {

        return new ArrayList<>(users);
    }
}