package com.example.iptimeAPI.domain.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByAccessToken(String accessToken);

    Optional<User> findByUserId(Long userId);

}
