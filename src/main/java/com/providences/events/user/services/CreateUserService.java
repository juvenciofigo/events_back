package com.providences.events.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.config.TokenService;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.AuthUserDTO;

@Service
public class CreateUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public AuthUserDTO.Response execute(AuthUserDTO.Request dto) {
        UserEntity user = UserEntity.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role(UserEntity.ROLE.ROLE_CLIENT)
                .build();

        this.userRepository.save(user);
        String token = tokenService.generateToken(user);

        return new AuthUserDTO.Response(
                user.getId(), user.getEmail(),
                user.getName(),
                user.getProfilePicture(),
                user.getAuthorities().stream().map(auth -> auth.getAuthority()).toList(),
                token);
    }

}
