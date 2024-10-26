package org.example.filter;


import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.JwtValid;
import org.example.jwt.TokenUser;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.URI;


@Component
@Slf4j

public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {
    private final JwtValid jwtValid;
    private final GlobalExceptionHandler globalExceptionHandler;
    public JwtAuthenticationGatewayFilterFactory(JwtValid jwtValid, GlobalExceptionHandler globalExceptionHandler) {
        super(Config.class);
        this.jwtValid = jwtValid;
        this.globalExceptionHandler = globalExceptionHandler;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst(config.header);
            if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(config.grant+" ")) {
                String token = authorizationHeader.substring(7); // Bearer
                log.info(token);
                try {
                    if (jwtValid.validateToken(token)) {
                        TokenUser user = jwtValid.decode(token);
                        log.info(user.getId());
                        log.info(user.getRole());
                        ServerWebExchange req= exchange.mutate()
                                .request(addAuthorization(exchange.getRequest(), user))
                                .build();
                        log.info(req.getRequest().getURI().toString());
                        return chain.filter(req); // 유효성 검사 통과후 성공 로직
                    }

                }catch (TokenExpiredException e) {
                    log.error("Token validation error: {}", e.getMessage());
                    return globalExceptionHandler.handle(exchange,e);
                }catch (ExpiredJwtException e) {
                    log.info("토큰 만료");
                    return globalExceptionHandler.handle(exchange,e);
                }
            }
            return onError(exchange); // 유효성 검사 실패 반환
        };
    }
    @Getter
    @Setter
    public static class Config{
        private String header="Authorization";
        private String grant="Bearer";
    }
    private ServerHttpRequest addAuthorization(ServerHttpRequest request, TokenUser tokenUser) {
        URI uri = URI.create(request.getURI()+"/"+tokenUser.getId());
        return request.mutate()
                .uri(uri)
                .build();
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }


}
