package com.providences.events.config;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class JWTUserData {
    String userId;
    String email;
    List<String> roles;

}
