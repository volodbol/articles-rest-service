package com.volod.articles.repository;

import com.volod.articles.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {

    @Query(value = "SELECT t FROM JwtToken t " +
            "INNER JOIN ApplicationUser u ON t.applicationUser.id = u.id " +
            "WHERE u.id = ?1 AND (t.expired = FALSE OR t.revoked = FALSE)")
    List<JwtToken> findAllValidTokenByUser(Integer id);

    Optional<JwtToken> findByToken(String token);

}
