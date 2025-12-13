package com.providences.events.presence;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class PresenceDisconnectListener
        implements ApplicationListener<SessionDisconnectEvent> {

    private final PresenceService presenceService;

    public PresenceDisconnectListener(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = null;
        try {
            userId = accessor.getNativeHeader("userId").get(0);
        } catch (Exception ignored) {
        }

        if (userId != null) {
            System.out.println("User disconnected: " + userId);
            presenceService.broadcastStatus(userId, "offline");
        }
    }
}