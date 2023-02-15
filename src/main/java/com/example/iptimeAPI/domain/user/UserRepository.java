package com.example.iptimeAPI.domain.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    /**
     * @param userId user의 id
     * @return userId를 기준으로 조회한 User 정보를 반환합니다.
     */
    Optional<User> findByUserId(Long userId);
}
