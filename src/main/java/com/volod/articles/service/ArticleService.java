package com.volod.articles.service;

import com.volod.articles.dto.ArticleDto;
import com.volod.articles.model.ApplicationUser;
import com.volod.articles.model.Article;
import com.volod.articles.model.ModelMapper;
import com.volod.articles.repository.ArticleRepository;
import com.volod.articles.request.CreateArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ModelMapper modelMapper;

    public ArticleDto getArticle(Integer id) {
        return modelMapper.toArticleDto(
                articleRepository.getOneById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Can not find article with id " + id))
        );
    }

    public ArticleDto saveArticle(CreateArticleRequest createArticleRequest, ApplicationUser applicationUser) {
        return modelMapper.toArticleDto(articleRepository.save(
                new Article()
                        .withText(createArticleRequest.getText())
                        .withColor(createArticleRequest.getColor())
                        .withApplicationUser(applicationUser)
        ));
    }

}
