package server;

import common.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<User> users = new ArrayList<>();

    public synchronized boolean register(User user) {

        if (user == null
                || user.getUsername() == null
                || user.getUsername().trim().isEmpty()
                || user.getPassword() == null
                || user.getPassword().isEmpty()) {

            return false;
        }

        String username = user.getUsername().trim();

        // Check duplicate username
        for (User existingUser : users) {

            if (existingUser.getUsername()
                    .equalsIgnoreCase(username)) {

                return false;
            }
        }

        // Hash password before storing
        String hashedPassword =
                PasswordHasher.hash(user.getPassword());

        User hashedUser =
                new User(
                        username,
                        hashedPassword
                );

        users.add(hashedUser);

        System.out.println(
                "User registered: " + username
        );

        System.out.println(
                "Total users: " + users.size()
        );

        return true;
    }

    public synchronized boolean login(
            String username,
            String password
    ) {

        if (username == null
                || password == null) {

            return false;
        }

        String hashedPassword =
                PasswordHasher.hash(password);

        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username)
                    && user.getPassword()
                    .equals(hashedPassword)) {

                return true;
            }
        }

        return false;
    }

    public synchronized int getUserCount() {
        return users.size();
    }
}