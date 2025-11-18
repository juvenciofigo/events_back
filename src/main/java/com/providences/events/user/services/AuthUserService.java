package com.providences.events.user.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.providences.events.config.token.RefreshTokenEntity;
import com.providences.events.config.token.RefreshTokenRepository;
import com.providences.events.config.token.TokenService;
import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Service
public class AuthUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CreateRefreshToken createRefreshToken;
    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthUserService(UserRepository userRepository,
            TokenService tokenService,
            CreateRefreshToken createRefreshToken,
            AuthenticationConfiguration authenticationConfiguration,
            RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.createRefreshToken = createRefreshToken;
        this.authenticationConfiguration = authenticationConfiguration;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas!"));
    }

    public UserDTO.Response login(UserDTO.Auth dto, String ip, String userAgent) {
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            UserEntity user = (UserEntity) authentication.getPrincipal();
            String token = tokenService.generateToken(user);

            // Criar RefreshToken
            String refreshToken = createRefreshToken.execute(user.getId(), ip, userAgent);

            return UserDTO.Response.response(user, token, refreshToken);

        } catch (AuthenticationException ex) {
            throw new UsernameNotFoundException("Credenciais inválidas!");
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao autenticar usuário", ex);
        }
    }

    public UserDTO.Response refreshToken(
            String cookieToken,
            HttpServletResponse resp,
            HttpServletRequest req) {

        Optional<RefreshTokenEntity> maybe = refreshTokenRepository.findByToken(cookieToken);

        if (maybe.isEmpty()) {
            createRefreshToken.clearRefreshCookie(resp);
            throw new BusinessException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }
        RefreshTokenEntity stored = maybe.get();
        if (stored.isRevoked() || tokenService.isExpired(stored)) {
            tokenService.revoke(stored);
            createRefreshToken.clearRefreshCookie(resp);
            throw new BusinessException("Refresh token expired or revoked", HttpStatus.UNAUTHORIZED);
        }

        // ROTACIONAR: revogar o antigo e criar novo
        tokenService.revoke(stored);

        UserEntity user = stored.getUser();

        String refreshToken = createRefreshToken.execute(user.getId(), req.getRemoteAddr(),
                req.getHeader("User-Agent"));

        String token = tokenService.generateToken(user);

        return UserDTO.Response.response(user, token, refreshToken);
    }

    public void logOut(String cookieToken, HttpServletResponse resp) {
        if (cookieToken != null)

        {
            refreshTokenRepository.findByToken(cookieToken).ifPresent(rt -> {
                tokenService.revoke(rt);
            });
        }

        createRefreshToken.clearRefreshCookie(resp);

    }

}
