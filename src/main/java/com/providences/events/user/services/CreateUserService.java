package com.providences.events.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.config.TokenService;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.AuthUserDTO;
import com.providences.events.user.dto.RegisterUserDTO;

@Service
@Transactional
public class CreateUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public AuthUserDTO.Response execute(RegisterUserDTO.Request data) {
        Boolean existUser = userRepository.findByEmail(data.getEmail()).isPresent();
        if (existUser) {
            throw new BusinessException("Email em uso! Experimento com um email diferente!", HttpStatus.CONFLICT);
        }

        UserEntity user = new UserEntity();
        user.setEmail(data.getEmail());
        user.setPasswordHash(passwordEncoder.encode(data.getPassword()));
        user.setPhone(data.getPhone());
        user.setName(data.getName());

        this.userRepository.save(user);
        String token = tokenService.generateToken(user);

        return AuthUserDTO.Response.response(user, token);
    }

}
