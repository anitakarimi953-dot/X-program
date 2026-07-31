package server;

import common.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private final List<User> users = new ArrayList<>();

    public boolean register(User user) {

        for (User existingUser : users) {
            if (existingUser.getUsername()
                    .equals(user.getUsername())) {

                return false;
            }
        }

        String hashedPassword =
                PasswordHasher.hash(user.getPassword());

        User hashedUser = new User(
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

        String hashedPassword =
                PasswordHasher.hash(password);

        for (User user : users) {

            if (user.getUsername().equals(username)
                    && user.getPassword().equals(hashedPassword)) {

                return true;
            }
        }

        return false;
    }
}