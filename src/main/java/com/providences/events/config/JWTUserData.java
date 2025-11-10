package com.providences.events.config;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTUserData {
    String userId;
    String email;
    Set<String> roles = new HashSet<>();

}
