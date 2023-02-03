package com.example.iptimeAPI.domain.user;

import lombok.Getter;

/**
 * IDP에서 조회한 User의 정보를 담은 객체입니다.
 */
@Getter
public class UserInfoVO {

    private Long year;

    private String name;

    private Long id;


    public UserInfoVO(Long year, String name, Long id) {
        this.year = year;
        this.name = name;
        this.id = id;
    }

}
