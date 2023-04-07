package com.volod.articles.configuration;

import com.volod.articles.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final RequestMatcher PROTECTED_URL = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/v1/article/**"),
            new AntPathRequestMatcher("/api/v1/users/**")
    );

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .anonymous().disable()
                .authorizeHttpRequests()
                .requestMatchers(PROTECTED_URL).authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .logout()
                .logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .csrf().disable()
                .cors().disable()
                .rememberMe().disable();
        return http.build();
    }

    private static LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication) -> SecurityContextHolder.clearContext();
    }

}
