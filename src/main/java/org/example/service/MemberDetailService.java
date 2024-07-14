package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberDetails;
import org.example.entity.Member;
import org.example.repository.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public MemberDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member= Optional.of(memberRepository.findByEmail(username).get()).orElseThrow(()->new UsernameNotFoundException("회원가입이 되있지 않았습니다"));
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
        return MemberDetails.builder()
                .email(member.getEmail())
                .authorities(authorities)
                .password(member.getPassword())
                .build();
    }
}
