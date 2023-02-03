package com.example.iptimeAPI.service.user;

import com.example.iptimeAPI.domain.user.User;
import com.example.iptimeAPI.domain.user.UserRepository;
import com.example.iptimeAPI.domain.user.UserService;
import com.example.iptimeAPI.service.user.exception.OuterServiceException;
import com.example.iptimeAPI.service.user.exception.OuterServiceValidateException;
import com.example.iptimeAPI.service.user.fegin.FeignUserInfo;
import com.example.iptimeAPI.domain.user.UserInfoVO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final FeignUserInfo feignUserInfo;


    @Override
    public UserInfoVO getUserByToken(String accessToken) {
        try {
            return feignUserInfo.getUserInfoByToken(accessToken);
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
            UserInfoVO userInfo = feignUserInfo.getUserInfo(userId);

            User user = User.create(userInfo);

            repository.save(user);

            return userInfo;
        } catch (Exception e) {
            throw new OuterServiceValidateException(OuterServiceException.IDP_EXCEPTION);
        }
    }

    @Override
    public List<UserInfoVO> getUsersById(List<Long> userIds) {
        List<UserInfoVO> userInfoVOS = new ArrayList<>();
        for (Long userId : userIds) {
            userInfoVOS.add(
                getUserById(userId)
            );
        }
        return userInfoVOS;
    }

    @Profile("dev")
    public UserInfoVO getUserById_dev(Long userId) {
        return new UserInfoVO(20L, "test" + userId, userId);
    }

}
