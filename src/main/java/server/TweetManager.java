package server;

import common.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetManager {

    private final List<Tweet> tweets = new ArrayList<>();
    private int nextId = 1;

    public synchronized Tweet createTweet(
            String username,
            String content
    ) {

        if (username == null || username.isBlank()) {
            return null;
        }

        if (content == null || content.isBlank()) {
            return null;
        }

        Tweet tweet = new Tweet(
                nextId++,
                username,
                content,
                java.time.LocalDateTime.now()
        );

        tweets.add(tweet);

        return tweet;
    }

    public synchronized List<Tweet> getAllTweets() {

        return new ArrayList<>(tweets);
    }

    public synchronized List<Tweet> getTweetsByUser(
            String username
    ) {

        List<Tweet> result = new ArrayList<>();

        for (Tweet tweet : tweets) {

            if (tweet.getUsername().equals(username)) {
                result.add(tweet);
            }
        }

        return result;
    }

    public synchronized boolean deleteTweet(
            int tweetId,
            String username
    ) {

        for (Tweet tweet : tweets) {

            if (tweet.getId() == tweetId
                    && tweet.getUsername().equals(username)) {

                tweets.remove(tweet);
                return true;
            }
        }

        return false;
    }
}