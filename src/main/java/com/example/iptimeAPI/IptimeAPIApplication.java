package com.example.iptimeAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableAsync
public class IptimeAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(IptimeAPIApplication.class, args);
    }

}
