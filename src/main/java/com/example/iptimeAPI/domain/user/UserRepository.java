package com.example.iptimeAPI.domain.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    /**
     * userId를 기반으로 캐싱된 User 정보를 조회하는 메서드입니다.
     * @param userId user의 id입니다.
     * @return userId를 기반으로 캐싱된 User 정보를 반환합니다.
     */
    Optional<User> findByUserId(Long userId);

}
