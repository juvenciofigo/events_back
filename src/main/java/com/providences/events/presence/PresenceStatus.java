package com.providences.events.presence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PresenceStatus  {
    private String userId;
    private String status; // "online" | "offline"
}
