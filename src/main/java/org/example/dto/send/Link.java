package org.example.dto.send;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Link {
    private String web_url;


    @Builder
    public Link(String web_url){
        this.web_url=web_url;

    }
}
