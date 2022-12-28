package com.example.iptimeAPI.util.iptime.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:iptime-html.properties")
public class IptimeConfigHTML {

    @Value("${tag.tbody}")
    private String tbody;

    @Value("${tag.input}")
    private String input;

    @Value("${tag.tr}")
    private String tr;

    @Value("${tag.td}")
    private String td;

    @Value("${tag.style}")
    private String style;

}
