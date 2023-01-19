package com.example.iptimeAPI.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDTO {
    private Long year;
    private String name;
    private Long id;
}
