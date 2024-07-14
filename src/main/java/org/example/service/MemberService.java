package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProfileDto;
import org.example.dto.login.LoginSuccessDto;
import org.example.dto.member.MemberDto;
import org.example.dto.RefreshDto;
import org.example.dto.exception.ExceptionResponse;
import org.example.dto.signup.SignUpRes;
import org.example.entity.Member;
import org.example.entity.Token;
import org.example.jwt.JwtDto;
import org.example.jwt.JwtProvider;
import org.example.repository.follow.FollowRepository;
import org.example.repository.member.MemberRepository;
import org.example.repository.token.TokenRepository;
import org.example.service.storage.NcpStorageService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final AuthenticationProvider authenticationProvider;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final NcpStorageService ncpStorageService;


    @Transactional
    public SignUpRes join(MemberDto memberDto, MultipartFile profileImg) throws IOException {
            if(!profileImg.isEmpty()){
                // ncp로 변경
                String file_name=ncpStorageService.imageUpload(profileImg);
                memberDto.setProfileImage(file_name);
            }
            if(duplicateEmail(memberDto.getEmail())){
                return SignUpRes.builder().message("이미 가입된 회원입니다").state("중복 가입").build();
            }
            if (duplicateNickName(memberDto.getNickName())) {
            return SignUpRes.builder().message("이미 사용 중인 닉네임입니다").state("중복 닉네임").build();
            } //이 부분이 , 그떄 작성만되고 적용이 안되어있었습니다. 추가했습니다!
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

            memberDto.setSocialType(0);

            Member member = Member.builder()
                    .memberDto(memberDto)
                    .build();
            memberRepository.save(member);
            return SignUpRes.builder()
                    .message("회원가입 되었습니다")
                    .state("처리 성공")
                    .build();
    }

    public LoginSuccessDto login(MemberDto memberDto){
        Optional<Member> member = memberRepository.findByEmail(memberDto.getEmail());
        member.orElseThrow();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberDto.getEmail(),memberDto.getPassword());
        Authentication auth = authenticationProvider.authenticate(token);
        JwtDto jwtDto = jwtProvider.createToken(auth);
        tokenRepository.save(Token.builder().email(memberDto.getEmail()).refreshToken(jwtDto.getRefreshToken()).build());
        return LoginSuccessDto.builder()
                .message("로그인 성공")
                .jwtDto(jwtDto).
                build();
        }

    public RefreshDto refreshToken(String refreshToken){
        log.info(refreshToken);
        Token token = tokenRepository.findByRefreshToken(refreshToken);
        return RefreshDto.builder().access_token(jwtProvider.recreateToken(token.getEmail())).build();
    }

    @Transactional
    public void deleteRefresh(String email){
        tokenRepository.deleteByEmail(email);
    }

    public MemberDto myProfile(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow();
        return Member.toDto(member.get());
    }

    public ProfileDto profile(String nickName, String email) {
        log.info(nickName);
        log.info(email);
        Optional<Member> member = memberRepository.findByNickName(nickName);
        Optional<Member> me = memberRepository.findByEmail(email);
        member.orElseThrow();
        if (followRepository.existsByFollowingIdAndFollowerId(member.get().getMemberId(),me.get().getMemberId())){
            return ProfileDto.builder().follow(true).memberDto(Member.toDto(member.get())).build();
        }
        else{return ProfileDto.builder().follow(false).memberDto(Member.toDto(member.get())).build();}

    }

    public String profileImg(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        return member.map(Member::getProfileImage).orElse(null);
    }

    public boolean duplicateNickName(String nickName){
        return memberRepository.findByNickName(nickName).isPresent();
    }

    public boolean duplicateEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    public String getNickName(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.map(Member::getNickName).orElse(null);

    }

    public ExceptionResponse updateProfile(MultipartFile profileImg, MemberDto memberDto, String email) throws IOException {
        Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow();
        String file_name= ncpStorageService.imageUpload(profileImg);
        memberDto.setProfileImage(file_name);
        //주석 처리 부는, ncp로 기존 gcp를 변경한 것입니다.
        ncpStorageService.imageDelete(email);
        memberRepository.updateInfo(Member.builder().memberDto(memberDto).build());
        return ExceptionResponse.builder()
                .state("success")
                .message("회원 정보 수정에 성공했습니다.")
                .build();
    }

    public List<String> autoComplete(String word) {
        return memberRepository.findMemberKeyWord(word);
    }

    public String getEmail(String nickName){
        Optional<Member> member = memberRepository.findEmailByNickName(nickName);
        return member.map(Member::getEmail).orElse(null);
    }

    public List<String> getnickNameList(char gender)
    {
        return memberRepository.findDistinctNickNamesByGender(gender);
    }
}
