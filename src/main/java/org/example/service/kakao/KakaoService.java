package org.example.service.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.ParseException;
import org.example.dto.member.MemberDto;
import org.example.dto.send.TemplateObject;
import org.example.entity.Member;
import org.example.entity.Token;
import org.example.jwt.JwtDto;
import org.example.jwt.JwtProvider;
import org.example.jwt.KakaoToken;
import org.example.repository.member.MemberRepository;
import org.example.repository.token.TokenRepository;
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
@RequiredArgsConstructor
@Slf4j
public class KakaoService {
    private final KakaoApi kakaoApi;
    private final KakaoFeign kakaoFeign;
    private final MemberRepository memberRepository;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    private final String Content_type ="application/x-www-form-urlencoded;charset=utf-8";
    private final String grant_type = "authorization_code";
    private final String client_id = "b9759cba8e0cdd5bcdb9d601f5a10ac1";
    private final String logout_redirect ="http://default-front-07385-26867304-b1e33c76cd35.kr.lb.naverncp.com:30";
    private final String secret ="8VCVTZpYOA21l7wgaKiqQa74q02S6pYI";
    private KakaoToken kakaoToken_user;
    public JwtDto GenerateToken(String code, String role) throws ParseException, IOException, org.json.simple.parser.ParseException {
        String email = OAuthSignUp(code,role);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,"default1234");
        Authentication authentication = authenticationProvider.authenticate(token);
        JwtDto jwtDto = jwtProvider.createToken(authentication);
        tokenRepository.save(Token.builder().refreshToken(jwtDto.getRefreshToken()).email(email).build());
        return jwtDto;
    }

    public KakaoToken getToken(String code, String role) throws JsonProcessingException {
        String login_redirect_member ="http://default-front-07385-26867304-b1e33c76cd35.kr.lb.naverncp.com:30/user/login/oauth2/kakao";
        String login_redirect_teacher ="http://default-front-07385-26867304-b1e33c76cd35.kr.lb.naverncp.com:30/user/teacherlogin/oauth2/kakao";
        String login_redirect = role.equals("ROLE_MEMBER")?login_redirect_member : login_redirect_teacher;
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken kakaoToken=objectMapper.readValue(kakaoFeign.getAccessToken(Content_type,grant_type,client_id,login_redirect,code,secret), KakaoToken.class);
        kakaoToken_user=kakaoToken;
        return kakaoToken;
    }

    public String getkakaoInfo(String code, String role) throws ParseException, JsonProcessingException {
        return kakaoApi.getUSerInfo("Bearer "+getToken(code, role).getAccessToken());
    }

    @Transactional
    public String OAuthSignUp(String code,String role) throws ParseException, IOException, org.json.simple.parser.ParseException {
        String user = getkakaoInfo(code,role);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(user);
        JSONObject kakaoAccount=(JSONObject) jsonObject.get("kakao_account");
        JSONObject properties=(JSONObject) jsonObject.get("properties");
        MemberDto memberDto =MemberDto.builder()
                .email(kakaoAccount.get("email").toString())
                .profileImage(properties.get("profile_image").toString())
                .nickName(properties.get("nickname").toString())
                .userName(properties.get("nickname").toString())
                .password(passwordEncoder.encode("default1234"))
                .memberInfo("안녕하세요 신규 회원입니다.")
                .socialType(1)
                .role(role)
                .build();
        Optional<Member> member = memberRepository.findByEmail(memberDto.getEmail());
        Member member1 = Member.builder()
                .memberDto(memberDto)
                .build();
        if (member.isEmpty()){
            log.info("첫 사용자");
            memberRepository.save(member1);
        } else if (member1.getRole().equals(member.get().getRole()))
        {
            //잘 입력 했다면 변경 정보
            log.info(" 잘 입력했어요, 이전 멤버랑 같은 role로 입력");
            memberRepository.updateInfo(member1);
        } //잘 입력 안하면 저장 X (이미 있던 것 사용)

        log.info(" 잘 입력했어요, 이전 멤버랑 같은 role로 입력");
       return memberDto.getEmail();
    }

    public void sendRealImage(TemplateObject templateObject) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String template = "template_object="+objectMapper.writeValueAsString(templateObject);
        kakaoApi.sendImage("Bearer "+ kakaoToken_user.getAccessToken(),"application/x-www-form-urlencoded",template);
    }

    public String kakaoLogOut(){
        return kakaoFeign.logOut(client_id,logout_redirect);
    }
}
