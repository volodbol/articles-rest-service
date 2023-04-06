package com.volod.articles.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@With
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", initialValue = 1_000_000)
    private Integer id;


    @Column(unique = true)
    private String name;

    private Integer age;

    @OneToMany(mappedBy = "applicationUser")
    @ToString.Exclude
    private List<Article> articles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ApplicationUser applicationUser = (ApplicationUser) o;
        return getId() != null && Objects.equals(getId(), applicationUser.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
