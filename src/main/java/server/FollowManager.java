package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowManager {

    // username -> list of users they follow
    private final Map<String, List<String>> following =
            new HashMap<>();

    // =========================
    // FOLLOW
    // =========================

    public boolean follow(
            String follower,
            String target
    ) {

        // Cannot follow yourself
        if (follower.equals(target)) {
            return false;
        }

        List<String> list =
                following.computeIfAbsent(
                        follower,
                        k -> new ArrayList<>()
                );

        // Already following
        if (list.contains(target)) {
            return false;
        }

        list.add(target);

        return true;
    }

    // =========================
    // UNFOLLOW
    // =========================

    public boolean unfollow(
            String follower,
            String target
    ) {

        List<String> list =
                following.get(follower);

        if (list == null) {
            return false;
        }

        return list.remove(target);
    }

    // =========================
    // GET FOLLOWING
    // =========================

    public List<String> getFollowing(
            String username
    ) {

        List<String> list =
                following.get(username);

        if (list == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(list);
    }

    // =========================
    // IS FOLLOWING
    // =========================

    public boolean isFollowing(
            String follower,
            String target
    ) {

        List<String> list =
                following.get(follower);

        return list != null
                && list.contains(target);
    }
}