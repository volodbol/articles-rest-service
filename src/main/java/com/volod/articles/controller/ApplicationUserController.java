package com.volod.articles.controller;

import com.volod.articles.dto.ApplicationUserDto;
import com.volod.articles.model.Color;
import com.volod.articles.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class ApplicationUserController {

    private final ApplicationUserService applicationUserService;

    @GetMapping
    public Page<ApplicationUserDto> getUsersWith(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "5") int pageSize,
            @RequestParam(name = "ageMoreThan", required = false) Integer age,
            @RequestParam(name = "article_color", required = false) Color color) {
        if (age != null) {
            return applicationUserService.getUsersWithAgeMoreThan(age, PageRequest.of(page, pageSize));
        } else if (color != null) {
            return applicationUserService.getUsersWhereArticlesHasColor(color, PageRequest.of(page, pageSize));
        } else {
            return applicationUserService.getAllUsers(PageRequest.of(page, pageSize));
        }
    }

    @GetMapping("most-active")
    public Page<String> getUsersNameWithMoreThanThreeArticles(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "page_size", defaultValue = "5") int pageSize) {
        return applicationUserService.getUsersNameWithMoreThanThreeArticles(PageRequest.of(page, pageSize));
    }

}
