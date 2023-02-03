package com.example.iptimeAPI.domain.user;

import java.util.List;

public interface UserService {

    UserInfoVO getUserByToken(String accessToken);

    UserInfoVO getUserById(Long userId);

    List<UserInfoVO> getUsersById(List<Long> userIds);

}
