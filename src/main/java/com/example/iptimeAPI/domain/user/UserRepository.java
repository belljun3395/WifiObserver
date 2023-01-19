package com.example.iptimeAPI.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByAccessToken(String accessToken);

    Optional<User> findByUserId(Long userId);
}
