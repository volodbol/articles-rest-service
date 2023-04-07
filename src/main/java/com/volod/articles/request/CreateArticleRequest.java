package com.volod.articles.request;

import com.volod.articles.model.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateArticleRequest {

    private String text;

    private Color color;

}
