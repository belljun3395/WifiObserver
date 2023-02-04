package com.example.iptimeAPI.domain.user;

import java.util.List;

public interface UserService {

    /**
     * @param accessToken IDP 서버에 조회할 accessToken
     * @return IDP 서버에서 조회한 UserInfo를 반환합니다.
     */
    UserInfoVO getUserByToken(String accessToken);

    /**
     * @param userId user의 id
     * @return IDP 서버에서 조회한 UserInfo를 반환합니다.
     */
    UserInfoVO getUserById(Long userId);

    /**
     * @param userIds 다수의 user의 id
     * @return IDP 서버에서 조회한 다수의 UserInfo를 반환합니다.
     */
    List<UserInfoVO> getUsersById(List<Long> userIds);

}
