package com.volod.articles.service;

import com.volod.articles.repository.JwtTokenRepository;
import com.volod.articles.security.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private static final int TOKEN_BEGIN_INDEX = 7;

    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(TOKEN_BEGIN_INDEX);
        jwtTokenRepository.findByToken(jwt)
                .ifPresent((JwtToken jwtToken) -> {
                    jwtToken.setExpired(true);
                    jwtToken.setRevoked(true);
                    jwtTokenRepository.save(jwtToken);
                    SecurityContextHolder.clearContext();
                });
    }
}
