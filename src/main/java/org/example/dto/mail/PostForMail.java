package org.example.dto.mail;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PostForMail {
    private String post_image;
    private String post_name;

}
