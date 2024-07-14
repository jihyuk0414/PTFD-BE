package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.member.MemberDto;
import org.example.dto.oauth2.OAuthAttributes;
import org.example.dto.oauth2.OAuthMember;
import org.example.entity.Member;
import org.example.repository.member.MemberRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {
    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        log.info(oAuth2User.getName());
        Map<String, Object> originAttributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attrs = oAuth2User.getAttributes();

        OAuthMember oAuthMember = OAuthAttributes.extract(registrationId, attrs);

        oAuthMember.setProvider(registrationId);

        Map<String, Object> customAttribute = customAttribute(attrs, userNameAttributeName, oAuthMember, registrationId);
        log.info(customAttribute.toString());


        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttribute,
                userNameAttributeName);
    }
    private Map<String, Object> customAttribute(Map<String, Object> attributes, String userNameAttributeName, OAuthMember oAuthMember, String registrationId) {
        Map<String, Object> customAttribute = new LinkedHashMap<>();
        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", oAuthMember.getName());
        customAttribute.put("email", oAuthMember.getEmail());
        customAttribute.put("image",oAuthMember.getProfile_image());
        return customAttribute;

    }
    private void saveOrUpdate(Map<String, Object> customAttribute){
        Optional<Member> m = memberRepository.findByEmail(customAttribute.get("email").toString());
        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(customAttribute.get("email").toString());
        memberDto.setNickName(customAttribute.get("name").toString());
        memberDto.setProfileImage(customAttribute.get("image").toString());
        Member member = Member.builder()
                .memberDto(memberDto)
                .build();
        if(m.isEmpty()) {memberRepository.save(member);}
        memberRepository.updateInfo(member);
    }
}
