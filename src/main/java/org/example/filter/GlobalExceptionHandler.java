package org.example.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jwt.TokenError;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Order(-1)
@Slf4j
@Component
public class GlobalExceptionHandler  implements ErrorWebExceptionHandler {


    private final ObjectMapper objectMapper;


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.info("global 예외 처리 동작");
        log.info(ex.getMessage());
        if (ex instanceof ExpiredJwtException) {
            log.info("토큰 만료 예외 처리 동작");
            TokenError token = TokenError.builder().state("Jwt Expired").build();
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory().wrap(objectMapper.writeValueAsBytes(token))));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }

    }
}
