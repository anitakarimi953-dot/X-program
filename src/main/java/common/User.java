package common;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;

    private List<String> following;

    public User(
            String username,
            String password
    ) {
        this.username = username;
        this.password = password;
        this.following = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username
    ) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password
    ) {
        this.password = password;
    }

    public List<String> getFollowing() {

        if (following == null) {
            following = new ArrayList<>();
        }

        return following;
    }

    public void setFollowing(
            List<String> following
    ) {

        if (following == null) {
            this.following =
                    new ArrayList<>();
        } else {
            this.following =
                    following;
        }
    }
}