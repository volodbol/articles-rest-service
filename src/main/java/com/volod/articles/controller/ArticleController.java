package com.volod.articles.controller;

import com.volod.articles.dto.ArticleDto;
import com.volod.articles.model.ApplicationUser;
import com.volod.articles.request.CreateArticleRequest;
import com.volod.articles.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("{id}")
    public ResponseEntity<ArticleDto> getArticleDto(@PathVariable Integer id) {
        return ResponseEntity.ok(articleService.getArticle(id));
    }

    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(
            @RequestBody CreateArticleRequest createArticleRequest,
            @AuthenticationPrincipal ApplicationUser applicationUser) {
        ArticleDto articleDto = articleService.saveArticle(createArticleRequest, applicationUser);
        return ResponseEntity.created(
                URI.create("api/v1/article/%s".formatted(articleDto.getId()))
        ).body(articleDto);
    }

}
