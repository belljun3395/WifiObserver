package com.example.iptimeAPI.config.iptime;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * iptime 설정페이지에서 조회한 HTML 페이지를 파싱하기 위한 요소를 설정하기 위한 클래스입니다.
 */
@Getter
@Component
@PropertySource("classpath:iptime-html.properties")
public class IptimeHTMLConfig {
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
