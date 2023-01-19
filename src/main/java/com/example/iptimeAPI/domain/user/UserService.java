package com.example.iptimeAPI.domain.user;

import com.example.iptimeAPI.service.user.dto.UserInfoDTO;

public interface UserService {

    UserInfoDTO getUserByToken(String accessToken);

    UserInfoDTO getUserById(Long userId);

}
