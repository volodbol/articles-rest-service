package com.volod.articles.repository;

import com.volod.articles.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
}
