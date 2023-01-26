package com.example.iptimeAPI.domain.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

// todo spring cache 적용후 삭제
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByAccessToken(String accessToken);

    Optional<User> findByUserId(Long userId);
}
