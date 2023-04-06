package com.volod.articles;

import com.volod.articles.model.ApplicationUser;
import com.volod.articles.model.Article;
import com.volod.articles.model.Color;
import com.volod.articles.repository.ArticleRepository;
import com.volod.articles.repository.ApplicationUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class ArticlesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticlesApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(
            ArticleRepository articleRepository,
            ApplicationUserRepository applicationUserRepository) {
        return (String[] args) -> {
            List<ApplicationUser> users = generateApplicationUsers(5);
            applicationUserRepository.saveAll(users);
            users.forEach(applicationUser -> IntStream.range(0, 5)
                            .mapToObj(number -> new Article()
                                    .withApplicationUser(applicationUser)
                                    .withText("random text " + number)
                                    .withColor(Color.values()[number]))
                            .forEach(articleRepository::save));
        };
    }

    private static List<ApplicationUser> generateApplicationUsers(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(number -> new ApplicationUser()
                        .withName("user" + number)
                        .withAge(number * 2 + 18)
                )
                .toList();
    }

}
