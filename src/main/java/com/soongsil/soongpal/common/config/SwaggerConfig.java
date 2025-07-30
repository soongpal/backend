package com.soongsil.soongpal.common.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("SoongPal API 문서")
                .version("v1.0.0")
                .description("숭실대학교 학생들을 위한 공동구매와 중고거래 플랫폼 입니다. 이 API 문서는 SoongPal의 API 사용법을 설명합니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

}
