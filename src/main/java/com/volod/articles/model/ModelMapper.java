package com.volod.articles.model;

import com.volod.articles.dto.ApplicationUserDto;
import com.volod.articles.dto.ArticleDto;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public ApplicationUserDto toApplicationUserDto(ApplicationUser applicationUser) {
        return ApplicationUserDto.builder()
                .id(applicationUser.getId())
                .username(applicationUser.getUsername())
                .age(applicationUser.getAge())
                .build();
    }

    public ArticleDto toArticleDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .text(article.getText())
                .color(article.getColor())
                .applicationUserDto(toApplicationUserDto(article.getApplicationUser()))
                .build();
    }
}
