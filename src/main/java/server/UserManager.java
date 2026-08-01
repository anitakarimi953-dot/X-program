package server;

import common.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<User> users =
            new ArrayList<>();

    // =========================================================
    // REGISTER
    // =========================================================

    public boolean register(User user) {

        if (user == null) {
            return false;
        }

        String username =
                user.getUsername();

        String password =
                user.getPassword();

        if (username == null ||
                password == null) {

            return false;
        }

        username = username.trim();

        if (username.isEmpty() ||
                password.isEmpty()) {

            return false;
        }

        // Check duplicate username
        for (User existingUser : users) {

            if (existingUser.getUsername()
                    .equalsIgnoreCase(username)) {

                return false;
            }
        }

        // Hash password before storing
        String hashedPassword =
                PasswordHasher.hash(password);

        User hashedUser =
                new User(
                        username,
                        hashedPassword
                );

        users.add(hashedUser);

        System.out.println(
                "Registered user: " + username
        );

        return true;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public boolean login(
            String username,
            String password
    ) {

        if (username == null ||
                password == null) {

            return false;
        }

        username = username.trim();

        if (username.isEmpty() ||
                password.isEmpty()) {

            return false;
        }

        String hashedPassword =
                PasswordHasher.hash(password);

        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username)
                    &&
                    user.getPassword()
                            .equals(hashedPassword)) {

                System.out.println(
                        "User logged in: " + username
                );

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // CHECK USER
    // =========================================================

    public boolean exists(String username) {

        if (username == null) {
            return false;
        }

        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username.trim())) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // GET USER
    // =========================================================

    public User getUser(String username) {

        if (username == null) {
            return null;
        }

        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username.trim())) {

                return user;
            }
        }

        return null;
    }

    // =========================================================
    // USER COUNT
    // =========================================================

    public int getUserCount() {

        return users.size();
    }
}