package com.volod.articles;

import com.volod.articles.model.ApplicationUser;
import com.volod.articles.model.Article;
import com.volod.articles.model.Color;
import com.volod.articles.repository.ApplicationUserRepository;
import com.volod.articles.repository.ArticleRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Articles",
        version = "1.0"
))
public class ArticlesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticlesApplication.class, args);
    }

    @Bean
    @Profile({"dev"})
    public CommandLineRunner runner(
            ArticleRepository articleRepository,
            ApplicationUserRepository applicationUserRepository,
            PasswordEncoder passwordEncoder) {
        return (String[] args) -> {
            List<ApplicationUser> users = generateApplicationUsersPlusDefault(5, passwordEncoder);
            applicationUserRepository.saveAll(users);
            users.forEach(applicationUser -> IntStream.range(0, 5)
                    .mapToObj(number -> new Article()
                            .withApplicationUser(applicationUser)
                            .withText("random text " + number)
                            .withColor(Color.values()[number]))
                    .forEach(articleRepository::save));
        };
    }

    private static List<ApplicationUser> generateApplicationUsersPlusDefault(
            int amount,
            PasswordEncoder passwordEncoder) {
        List<ApplicationUser> applicationUsers = IntStream.range(0, amount)
                .mapToObj(number -> new ApplicationUser()
                        .withUsername("user" + number)
                        .withPassword(passwordEncoder.encode("user" + number))
                        .withAge(number * 2 + 18)
                )
                .collect(Collectors.toList());
        applicationUsers.add(
                new ApplicationUser()
                        .withUsername("admin")
                        .withPassword(passwordEncoder.encode("admin"))
                        .withAge(22)
        );
        return applicationUsers;
    }

}
