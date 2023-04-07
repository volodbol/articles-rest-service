package com.volod.articles.dto;

import com.volod.articles.model.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ArticleDto {

    private Integer id;

    private String text;

    private Color color;

    private ApplicationUserDto applicationUserDto;

}
