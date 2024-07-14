package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TemplateObject {
    @JsonProperty("object_type")
    private String objType="text";
    private String text="구매 내역";
    @JsonProperty("web_url")
    private String webUrl;

    @Builder
    public TemplateObject(String webUrl){

        this.webUrl=webUrl;
    }
}
