package com.example.iptimeAPI.domain.user;

import com.example.iptimeAPI.service.user.dto.UserInfoVO;

public interface UserService {

    UserInfoVO getUserByToken(String accessToken);

    UserInfoVO getUserById(Long userId);

}
