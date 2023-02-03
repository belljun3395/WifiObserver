package com.example.iptimeAPI.domain.user;

import java.util.List;

public interface UserService {

    /**
     * accessToken을 가지고 IDP 서버에 UserInfo를 조회합니다.
     * @param accessToken IDP 서버에 조회할 accessToken 입니다.
     * @return IDP 서버에서 조회한 UserInfo를 반환합니다.
     */
    UserInfoVO getUserByToken(String accessToken);

    /**
     * userId를 가지고 IDP 서버에 UserInfo를 조회합니다.
     * @param userId user의 id입니다.
     * @return IDP 서버에서 조회한 UserInfo를 반환합니다.
     */
    UserInfoVO getUserById(Long userId);

    /**
     * 다수의 userId를 가지고 IDP 서버에 다수의 UserInfo를 조회합니다.
     * @param userIds 다수의 user의 id입니다.
     * @return IDP 서버에서 조회한 다수의 UserInfo를 반환합니다.
     */
    List<UserInfoVO> getUsersById(List<Long> userIds);

}
