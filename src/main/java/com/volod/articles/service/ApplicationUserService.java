package com.volod.articles.service;

import com.volod.articles.dto.ApplicationUserDto;
import com.volod.articles.model.ArticleApplicationUser;
import com.volod.articles.model.ArticleSummary;
import com.volod.articles.model.Color;
import com.volod.articles.model.ModelMapper;
import com.volod.articles.repository.ApplicationUserRepository;
import com.volod.articles.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    private final ArticleRepository articleRepository;

    private final ModelMapper modelMapper;


    public Page<ApplicationUserDto> getUsersWithAgeMoreThan(int age, Pageable pageable) {
        return applicationUserRepository.findAllByAgeGreaterThan(age, pageable)
                .map(modelMapper::toApplicationUserDto);
    }

    public Page<ApplicationUserDto> getUsersWhereArticlesHasColor(Color color, PageRequest pageable) {
        return articleRepository.findAllByColor(color, pageable)
                .map(ArticleApplicationUser::getApplicationUser)
                .map(modelMapper::toApplicationUserDto);
    }

    public Page<String> getUsersNameWithMoreThanThreeArticles(Pageable pageable) {
        return articleRepository.findAllByArticlesMoreThanThree(pageable)
                .map(ArticleSummary::getName);
    }

    public Page<ApplicationUserDto> getAllUsers(Pageable pageable) {
        return applicationUserRepository.findAll(pageable)
                .map(modelMapper::toApplicationUserDto);
    }

}
