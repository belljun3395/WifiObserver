package com.example.iptimeAPI.web.dto;

import lombok.Data;

@Data
public class IpResponseDTO {
    private boolean isIn;

    public IpResponseDTO(boolean isIn) {
        this.isIn = isIn;
    }
}
