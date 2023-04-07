package com.volod.articles.repository;

import com.volod.articles.model.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {

    Page<ApplicationUser> findAllByAgeGreaterThan(Integer age, Pageable pageable);

    Page<ApplicationUser> findAll(Pageable pageable);

    Boolean existsByUsername(String username);

    Optional<ApplicationUser> findByUsername(String username);

}
