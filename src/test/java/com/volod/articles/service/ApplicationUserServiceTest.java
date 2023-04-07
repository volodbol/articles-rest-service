package com.volod.articles.service;

import com.volod.articles.IntegrationTest;
import com.volod.articles.dto.ApplicationUserDto;
import com.volod.articles.model.ApplicationUser;
import com.volod.articles.model.Article;
import com.volod.articles.model.Color;
import com.volod.articles.model.ModelMapper;
import com.volod.articles.repository.ApplicationUserRepository;
import com.volod.articles.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@IntegrationTest
class ApplicationUserServiceTest {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private ApplicationUserService applicationUserService;

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setupTest() {
        applicationUserService = new ApplicationUserService(
                applicationUserRepository,
                articleRepository,
                modelMapper
        );
    }


    @Test
    void testWhenUsersPageWithAgeMoreThenRequestedThenPageMustBeReturned() {
        int amount = 10;
        int age = 24;
        saveApplicationUsers(amount);
        Page<ApplicationUserDto> userDtoPage = applicationUserService
                .getUsersWithAgeMoreThan(age, PageRequest.of(0, 5));

        int expectedUserDtoTotalNumber = 6;
        assertEquals(expectedUserDtoTotalNumber, userDtoPage.getTotalElements(),
                "Total amount of page objects should be equal!");
        int expectedPageAmount = 5;
        assertEquals(expectedPageAmount, userDtoPage.getNumberOfElements(),
                "Amount of page objects should be equal!");
    }

    @Test
    void testWhenUsersPageWithArticlesOfColorRequestedThenPageMustBeReturned() {
        int amount = 10;
        Color expectedColor = Color.BLACK;
        saveApplicationUsers(amount);
        applicationUserService.getAllUsers(PageRequest.of(0, amount))
                .forEach(applicationUserDto -> articleRepository.save(
                        new Article()
                                .withText("some text")
                                .withColor(expectedColor)
                                .withApplicationUser(
                                        applicationUserRepository.findById(applicationUserDto.getId()).get()
                                )
                ));

        Page<ApplicationUserDto> userDtoPage = applicationUserService
                .getUsersWhereArticlesHasColor(expectedColor, PageRequest.of(0, 5));

        assertEquals(amount, userDtoPage.getTotalElements(),
                "Total amount of page objects should be equal!");
        int expectedPageAmount = 5;
        assertEquals(expectedPageAmount, userDtoPage.getNumberOfElements(),
                "Amount of page objects should be equal!");
    }


    private void saveApplicationUsers(int amount) {
        for (int i = 0; i < amount; i++) {
            applicationUserRepository.save(new ApplicationUser()
                    .withUsername("user" + i)
                    .withPassword("user" + i)
                    .withAge(i * 2 + 18)
            );
        }
    }


}