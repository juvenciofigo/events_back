package com.providences.events.presence;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PresenceService {
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void broadcastStatus(String userId, String status) {
        PresenceStatus presence = new PresenceStatus(userId, status);
        messagingTemplate.convertAndSend("/topic/presence/" + userId, presence);
    }
}
