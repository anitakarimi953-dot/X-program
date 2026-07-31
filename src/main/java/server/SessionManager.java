package server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private final Map<String, String> sessions =
            new HashMap<>();

    public String createSession(String username) {

        String sessionId =
                UUID.randomUUID().toString();

        sessions.put(sessionId, username);

        return sessionId;
    }

    public boolean isValid(String sessionId) {

        return sessionId != null
                && sessions.containsKey(sessionId);
    }

    public String getUsername(String sessionId) {

        return sessions.get(sessionId);
    }

    public void removeSession(String sessionId) {

        sessions.remove(sessionId);
    }
}