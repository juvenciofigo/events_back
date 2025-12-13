package com.providences.events.presence;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Component
public class PresenceConnectListener implements ApplicationListener<SessionConnectEvent> {

    private final PresenceService presenceService;

    public PresenceConnectListener(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = null;
        try {
            userId = accessor.getNativeHeader("userId").get(0);
        } catch (Exception ignored) {
        }

        if (userId != null) {
            System.out.println("User connected: " + userId);
            presenceService.broadcastStatus(userId, "online");
        }
    }
}
