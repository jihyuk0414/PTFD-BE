package org.example.service.kakao;

import com.google.gson.JsonObject;
import org.example.TestConfig;
import org.example.dto.send.Content;
import org.example.dto.send.Link;
import org.example.dto.send.TemplateObject;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = TestConfig.class)
class KakaoServiceTest {
    @Test
    void json(){
        Content content = Content.builder()
                .title("test")
                .image_url("image")
                .link(Link.builder().web_url("image").build())
                .description("다락방에서 구매한 상품입니다.")
                .build();
        TemplateObject templateObject = TemplateObject.builder()
                .content(content)
                .build();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("template_object",templateObject);
        System.out.println(jsonObject);
    }

}