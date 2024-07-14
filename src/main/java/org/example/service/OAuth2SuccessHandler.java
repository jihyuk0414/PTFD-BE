package org.example.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.JwtDto;
import org.example.jwt.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;


    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        JwtDto jwtDto = jwtProvider.createToken(authentication);
        log.info("accessToken = {}", jwtDto.getAccessToken());

        response.addHeader("accessToken ",jwtDto.getAccessToken());
        response.addHeader("refreshToken ",jwtDto.getRefreshToken());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        log.info(authentication.getPrincipal().toString());
        response.getWriter().write(authentication.getPrincipal().toString());
        // accessToken을 Header에 담아 redirect
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/login/success")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        log.info("redirect 준비");
        // 로그인 확인 페이지로 리다이렉트 시킨다.
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
