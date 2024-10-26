package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChatMember {
    private String email;
    private String nick_name;
    private String role;
    private String profile_image;
    private String user_name;

    @Builder
    public ChatMember(String email,String nick_name,String role,String profile_image, String user_name){
        this.email=email;
        this.nick_name=nick_name;
        this.role=role;
        this.profile_image=profile_image;
        this.user_name=user_name;
    }
}
