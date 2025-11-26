package com.example.gym.repository;

import com.example.gym.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u " +
            "WHERE (:q IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%',:q,'%')) " +
            " OR LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%')) )")
    Page<User> search(@Param("q") String q, Pageable pageable);
}