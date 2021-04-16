package com.trodix.todoapi.repository;

import java.util.Optional;

import com.trodix.todoapi.entity.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Query("SELECT t FROM RefreshToken t JOIN t.user u WHERE t.token = :token AND u.username = :username")
    Optional<RefreshToken> findByTokenAndUsername(@Param("token") String token, @Param("username") String username);

    void deleteByToken(String token);

}