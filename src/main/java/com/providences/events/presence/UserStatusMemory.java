package com.providences.events.presence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserStatusMemory {
    private static final Map<String, Boolean> onlineUsers = new ConcurrentHashMap<>();

    public static void setOnline(String profileId) {
        onlineUsers.put(profileId, true);
    }

    public static void setOffline(String profileId) {
        onlineUsers.put(profileId, false);
    }

    public static boolean isOnline(String profileId) {
        return onlineUsers.getOrDefault(profileId, false);
    }
}
