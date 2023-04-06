package com.volod.articles.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@With
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARTICLE_SEQ")
    @SequenceGenerator(name = "ARTICLE_SEQ", initialValue = 1_000_000)
    private Integer id;

    private String text;

    @Enumerated(EnumType.STRING)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ApplicationUser applicationUser;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", color=" + color +
                ", user=" + applicationUser +
                '}';
    }
}
