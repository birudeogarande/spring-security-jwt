package com.saatvik.app.dao;

import com.saatvik.app.dto.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
