package server;

import java.util.*;

public class FollowManager {

    // username -> کسانی که این user دنبال می‌کند
    private final Map<String, Set<String>> following = new HashMap<>();

    // username -> کسانی که این user را دنبال می‌کنند
    private final Map<String, Set<String>> followers = new HashMap<>();

    public synchronized boolean follow(String follower, String target) {

        if (follower == null || target == null) {
            return false;
        }

        if (follower.equals(target)) {
            return false;
        }

        following.putIfAbsent(follower, new HashSet<>());
        followers.putIfAbsent(target, new HashSet<>());

        // اگر قبلاً دنبال کرده
        if (following.get(follower).contains(target)) {
            return false;
        }

        following.get(follower).add(target);
        followers.get(target).add(follower);

        return true;
    }

    public synchronized boolean unfollow(String follower, String target) {

        if (follower == null || target == null) {
            return false;
        }

        if (!following.containsKey(follower)
                || !following.get(follower).contains(target)) {
            return false;
        }

        following.get(follower).remove(target);

        if (followers.containsKey(target)) {
            followers.get(target).remove(follower);
        }

        return true;
    }

    public synchronized boolean isFollowing(
            String follower,
            String target
    ) {

        return following.containsKey(follower)
                && following.get(follower).contains(target);
    }

    public synchronized List<String> getFollowing(
            String username
    ) {

        if (!following.containsKey(username)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                following.get(username)
        );
    }

    public synchronized List<String> getFollowers(
            String username
    ) {

        if (!followers.containsKey(username)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                followers.get(username)
        );
    }
}