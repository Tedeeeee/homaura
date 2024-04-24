package com.example.apigatewayserver.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private Environment env;
    public static class Config { }

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "토큰이 존재하지 않습니다", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", "");

            String uuid = isJwtValid(token);

            if (uuid == null) {
                return onError(exchange, "토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED);
            }

            ServerHttpRequest newRequest = request.mutate().header("UUID", uuid).build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        }));
    }

    private Claims getClaimsFormToken(String token) {
        byte[] decode = Base64.getDecoder().decode(env.getProperty("jwt.secret.key").getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(decode);
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("토큰의 만료기간이 지났습니다");
        }
    }

    private String isJwtValid(String token) {
        Claims claims = getClaimsFormToken(token);
        String uuid = null;

        try {
            uuid = claims.get("memberUUID").toString();
        } catch (Exception e) {
            throw new RuntimeException("회원의 정보가 잘못되었습니다");
        }

        return uuid;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);

        return response.setComplete();
    }
}
