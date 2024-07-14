package org.example.dto.oauth2;

import lombok.Data;

@Data
public class OAuthMember {
    private String name;
    private String email;
    private String provider;
    private String nickname;
    private String profile_image;
}
