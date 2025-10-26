package com.providences.events.config;

import java.util.List;

public record JWTUserData(String userId, String email, List<String> roles) {
}
