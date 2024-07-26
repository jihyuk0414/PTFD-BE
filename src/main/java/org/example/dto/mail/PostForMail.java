package org.example.dto.mail;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostForMail {
    private String image_post;
    private String post_name;

}
