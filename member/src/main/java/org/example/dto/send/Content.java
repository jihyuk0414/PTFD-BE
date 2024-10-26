package org.example.dto.send;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Content{
    private String title;
    private String image_url;
    private String description;
    private Link link;

    @Builder
    public Content(String title, String image_url,String description, Link link){
        this.description=description;
        this.image_url=image_url;
        this.title=title;
        this.link=link;
    }
}
