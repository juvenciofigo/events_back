package com.providences.events.config;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.providences.events.config.token.JWTUserDTO;
import com.providences.events.config.token.TokenService;
import com.providences.events.shared.exception.exceptions.BusinessException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public SecurityFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    try {
        if (Strings.isNotEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());

            Optional<JWTUserDTO> optUser = tokenService.validateToken(token);

            if (optUser.isPresent()) {
                JWTUserDTO userData = optUser.get();

                Set<SimpleGrantedAuthority> authorities = userData.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .collect(Collectors.toSet());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userData,
                        null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);

    } catch (BusinessException ex) {
        response.setStatus(ex.getStatusCode().value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
    }
}

}
