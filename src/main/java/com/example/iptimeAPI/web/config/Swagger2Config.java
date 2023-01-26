package com.example.iptimeAPI.web.config;

import java.security.Timestamp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// todo api 요청 제한 구현
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket restAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.iptimeAPI.web.controller"))
            .paths(PathSelectors.any())
            .build()
            .directModelSubstitute(Timestamp.class, Long.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Econo Forest Iptime API")
            .version("1.0.0")
            .build();
    }

}
