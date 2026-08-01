package common;

public class Profile {

    private String username;
    private int followersCount;
    private int followingCount;

    public Profile() {
    }

    public Profile(
            String username,
            int followersCount,
            int followingCount
    ) {

        this.username = username;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(
            int followersCount
    ) {

        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(
            int followingCount
    ) {

        this.followingCount = followingCount;
    }
}