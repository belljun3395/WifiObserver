package com.example.iptimeAPI.service.user;

import com.example.iptimeAPI.domain.user.User;
import com.example.iptimeAPI.domain.user.UserRepository;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.service.user.dto.UserInfoVO;
import com.example.iptimeAPI.service.user.exception.OuterServiceException;
import com.example.iptimeAPI.service.user.exception.OuterServiceValidateException;
import com.example.iptimeAPI.service.user.fegin.FeignUserInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final FeignUserInfo feignUserInfo;

    @Override
    public UserInfoVO getUserByToken(String accessToken) {
        Optional<User> userByAccessToken = repository.findByAccessToken(accessToken);

        if (userByAccessToken.isPresent()) {
            return userByAccessToken
                .get()
                .getUserInfoVO();
        }

        try {
            UserInfoVO userInfoByToken = feignUserInfo.getUserInfoByToken(accessToken);

            User user = new User(accessToken, userInfoByToken);

            repository.save(user);

            return userInfoByToken;
        } catch (Exception e) {
            throw new OuterServiceValidateException(OuterServiceException.IDP_EXCEPTION);
        }
    }

    @Override
    public UserInfoVO getUserById(Long userId) {
        Optional<User> userByAccessToken = repository.findByUserId(userId);

        if (userByAccessToken.isPresent()) {
            return userByAccessToken
                .get()
                .getUserInfoVO();
        }

        try {
            return feignUserInfo.getUserInfo(userId);
        } catch (Exception e) {
            throw new OuterServiceValidateException(OuterServiceException.IDP_EXCEPTION);
        }
    }
}
