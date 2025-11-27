package com.providences.events.user.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.config.token.TokenService;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.UserDTO;

import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional
public class CreateUserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private final CreateRefreshToken createRefreshToken;

    public CreateUserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            TokenService tokenService, CreateRefreshToken createRefreshToken) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.createRefreshToken = createRefreshToken;
    }

    @Transactional
    public UserDTO.Response execute(UserDTO.Create data, String ip, String userAgent, HttpServletResponse resp) {
        Boolean existUser = userRepository.findByEmail(data.getEmail()).isPresent();
        if (existUser) {
            throw new BusinessException("Email em uso! Experimento com um email diferente!", HttpStatus.CONFLICT);
        }

        UserEntity user = new UserEntity();
        user.setEmail(data.getEmail());
        user.setPasswordHash(passwordEncoder.encode(data.getPassword()));
        user.setPhone(data.getPhone());
        user.setName(data.getName());

        UserEntity savedUser = this.userRepository.save(user);

        String token = tokenService.generateToken(savedUser);

        String refreshToken = createRefreshToken.execute(savedUser.getId(), ip, userAgent);
        resp.addHeader("Set-Cookie", refreshToken);
        return UserDTO.Response.response(user, token);
    }

}
