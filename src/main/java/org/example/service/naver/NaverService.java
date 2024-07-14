package org.example.service.naver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.ParseException;
import org.example.dto.member.MemberDto;
import org.example.entity.Member;
import org.example.jwt.JwtDto;
import org.example.jwt.JwtProvider;
import org.example.jwt.NaverToken;
import org.example.repository.member.MemberRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverService {
    private final NaverFeign naverFeign;
    private final NaverApi naverApi;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final String client_id="3OGCudku4yOAQaRE_ou3";
    private final String redirect_uri="http%3a%2f%2flocalhost%3a8080%2foauth2%2fnaver";
    //url 인코딩 값
    private final String client_secret="gVeTyZ76l7";
    private final String grant_type="authorization_code";
    //1) 발급:'authorization_code'
    //2) 갱신:'refresh_token'
    //3) 삭제: 'delete'
    private final String state="1234";
    public JwtDto GenerateToken(String code) throws ParseException, IOException, org.json.simple.parser.ParseException {
        String email = OAuthSignUp(code);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,"default1234");
        Authentication authentication = authenticationProvider.authenticate(token);
        return jwtProvider.createToken(authentication);
    }
    public NaverToken getAccessToken(String code) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NaverToken naverToken=objectMapper.readValue(naverFeign.getToken(client_id,client_secret,grant_type,code), NaverToken.class);
        log.info(naverToken.getAccessToken());
        return naverToken;
    }

    public String getNaverInfo(String code) throws JsonProcessingException {
        return naverApi.UserInfo("Bearer "+getAccessToken(code).getAccessToken());
    }
    @Transactional
    public String OAuthSignUp(String code) throws ParseException, IOException, org.json.simple.parser.ParseException {
        String user = getNaverInfo(code);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(user);
        JSONObject response=(JSONObject) jsonObject.get("response");

        MemberDto memberDto =MemberDto.builder()
                .email(response.get("email").toString())
                .profileImage(response.get("profile_image").toString())
                .userName(response.get("name").toString())
                .password(passwordEncoder.encode("default1234"))
                .socialType(2)
                .build();

        Optional<Member> member = memberRepository.findByEmail(memberDto.getEmail());
        Member member1 = Member.builder()
                .memberDto(memberDto)
                .build();


        if (member.isEmpty()){
            memberRepository.save(member1);
        }
        else {memberRepository.updateInfo(member1);}
        log.info("User DB 저장");

        return memberDto.getEmail();
    }
}
