package com.providences.events.user.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.providences.events.config.TokenService;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.AuthUserDTO;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Service
public class AuthUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthUserService(UserRepository userRepository, TokenService tokenService,
            AuthenticationConfiguration authenticationConfiguration) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas!"));
    }

    public AuthUserDTO.Response execute(AuthUserDTO.Request dto) {
        System.out.println("ffffffffffffffffffffffffff" + dto.getEmail());
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            UserEntity user = (UserEntity) authentication.getPrincipal();
            String token = tokenService.generateToken(user);

            return AuthUserDTO.Response.response(user, token);

        } catch (AuthenticationException ex) {
            throw new UsernameNotFoundException("Credenciais inválidas!");
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao autenticar usuário", ex);
        }
    }
}
