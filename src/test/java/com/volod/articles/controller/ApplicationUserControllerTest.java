package com.volod.articles.controller;

import com.volod.articles.IntegrationTest;
import com.volod.articles.dto.ApplicationUserDto;
import com.volod.articles.repository.JwtTokenRepository;
import com.volod.articles.security.JwtToken;
import com.volod.articles.service.ApplicationUserService;
import com.volod.articles.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ApplicationUserController.class})
@IntegrationTest
class ApplicationUserControllerTest {

    @MockBean
    private ApplicationUserService applicationUserService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupTest() {
        when(jwtService.extractUsername(anyString())).thenReturn("admin");
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtTokenRepository.findByToken(anyString())).thenReturn(
                Optional.of(
                        JwtToken.builder()
                                .expired(false)
                                .revoked(false)
                                .build()
                ));
    }

    @Test
    @WithMockUser
    void testWhenUsersPageWithAgeMoreRequestedThenPageMustBeReturned() throws Exception {
        int amount = 10;
        int age = 18;
        when(applicationUserService.getUsersWithAgeMoreThan(age, PageRequest.of(0, amount)))
                .thenReturn(generateApplicationUsersDto(amount));

        mockMvc.perform(get("/api/v1/users?ageMoreThan=%s&page_size=%s".formatted(age, amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.size()").value(amount))
                .andExpect(jsonPath("$.totalElements").value(amount));
    }

    private Page<ApplicationUserDto> generateApplicationUsersDto(int amount) {
        ArrayList<ApplicationUserDto> userDtos = new ArrayList<>();
        for (int i = 1; i <= amount; i++) {
            userDtos.add(
                    ApplicationUserDto.builder()
                            .id(i)
                            .username("name" + i)
                            .age(i * 2 + 18)
                            .build()
            );
        }
        return new PageImpl<>(userDtos);
    }

}