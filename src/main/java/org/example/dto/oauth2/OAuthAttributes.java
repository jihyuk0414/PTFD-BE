package org.example.dto.oauth2;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    NAVER("naver", (attributes) -> {

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        System.out.println(response);
        OAuthMember memberProfile = new OAuthMember();
        memberProfile.setName((String) response.get("name"));
        memberProfile.setEmail(((String) response.get("email")));
        memberProfile.setProfile_image((String)response.get("profile_image"));

        return memberProfile;
    }),

    KAKAO("kakao", (attributes) -> {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        OAuthMember memberProfile = new OAuthMember();
        memberProfile.setName((String) kakaoProfile.get("nickname"));
        memberProfile.setEmail((String) kakaoAccount.get("email"));
        memberProfile.setProfile_image((String) kakaoProfile.get("profile_image_url"));
        return memberProfile;
    });
    private final String registrationId;
    private final Function<Map<String, Object>, OAuthMember> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, OAuthMember> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static OAuthMember extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> registrationId.equals(provider.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
