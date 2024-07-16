package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI SwaggerAPI()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("오픈 마켓 API - 상품, 댓글 부")
                        .description("상품 게시글, 댓글의 CRUD를 확인합니다.")
                        .version("1.0.0")) ;

    }

}
