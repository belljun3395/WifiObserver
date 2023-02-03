package com.example.iptimeAPI.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
