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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Service
@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas!"));
    }

    public AuthUserDTO.Response execute(AuthUserDTO.Request dto) {
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            UserEntity user = (UserEntity) authentication.getPrincipal();
            String token = tokenService.generateToken(user);

            return new AuthUserDTO.Response(
                    user.getId(), user.getEmail(),
                    user.getName(),
                    user.getProfilePicture(),
                    user.getAuthorities().stream().map(auth -> auth.getAuthority()).toList(),
                    token);

        } catch (AuthenticationException ex) {
            throw new UsernameNotFoundException("Credenciais inválidas!");
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao autenticar usuário", ex);
        }
    }
}
