package com.volod.articles.service;

import com.volod.articles.model.ApplicationUser;
import com.volod.articles.repository.ApplicationUserRepository;
import com.volod.articles.repository.JwtTokenRepository;
import com.volod.articles.request.AuthenticationRequest;
import com.volod.articles.request.CreateUserRequest;
import com.volod.articles.response.AuthenticationResponse;
import com.volod.articles.security.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final int TOKEN_BEGIN_INDEX = 7;

    private final ApplicationUserRepository applicationUserRepository;

    private final JwtTokenRepository jwtTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(CreateUserRequest request) {
        if (Boolean.TRUE.equals(applicationUserRepository.existsByUsername(request.getUsername()))) {
            throw new IllegalArgumentException("Username is taken");
        }
        ApplicationUser applicationUser = applicationUserRepository.save(new ApplicationUser()
                .withUsername(request.getUsername())
                .withPassword(passwordEncoder.encode(request.getPassword()))
                .withAge(request.getAge()));
        String jwtToken = jwtService.generateToken(applicationUser);
        String refreshToken = jwtService.generateRefreshToken(applicationUser);
        saveUserToken(applicationUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        ApplicationUser user = applicationUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(ApplicationUser user, String jwtToken) {
        jwtTokenRepository.save(
                JwtToken.builder()
                        .applicationUser(user)
                        .token(jwtToken)
                        .expired(false)
                        .revoked(false)
                        .build()
        );
    }

    private void revokeAllUserTokens(ApplicationUser user) {
        List<JwtToken> validUserTokens = jwtTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach((JwtToken token) -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        jwtTokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CredentialsExpiredException("Credentials either expired or wrong");
        }

        String refreshToken = authHeader.substring(TOKEN_BEGIN_INDEX);
        String username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            ApplicationUser user = applicationUserRepository.findByUsername(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        throw new CredentialsExpiredException("Credentials either expired or wrong");
    }

}
