package com.example.apigatewayserver.filter;

import com.example.apigatewayserver.util.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private final RedisUtil redisUtil;
    private final Environment env;
    public static class Config { }

    @Autowired
    public AuthorizationHeaderFilter(RedisUtil redisUtil, Environment env) {
        super(Config.class);
        this.redisUtil = redisUtil;
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response;

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "토큰이 존재하지 않습니다", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String token = authorizationHeader.replace("Bearer ", "");

            String path = request.getURI().getPath();
            if ("/logout".equals(path)) {
                redisUtil.setBlackList(token, "accessToken", 60);
                return response(exchange, "로그아웃 되었습니다", HttpStatus.OK);
            }


            String uuid = isJwtValid(token, exchange);

            if (uuid == null) {
                return onError(exchange, "토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED);
            }

            if (uuid.equals("로그아웃 한 회원입니다")) {
                return response(exchange, "로그아웃 한 회원입니다", HttpStatus.UNAUTHORIZED);
            }

            if ("/password".equals(path)) {
                redisUtil.setBlackList(token, "accessToken", 60);
            }

            ServerHttpRequest newRequest = request.mutate().header("UUID", uuid).build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        }));
    }

    private String validateTokenAndGetUUID(String token,ServerWebExchange exchange) {
        byte[] decode = Base64.getDecoder().decode(env.getProperty("jwt.secret.key").getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(decode);
        try {
            Claims body = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            if (redisUtil.hasKeyBlackList(token)) {
                return "로그아웃 한 회원입니다";
            }
            return body.get("memberUUID").toString();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰의 만료기간이 지났습니다");
        }
    }

    private String isJwtValid(String token, ServerWebExchange exchange) {
        String uuid = null;
        try {
            uuid = validateTokenAndGetUUID(token,exchange);
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

    private Mono<Void> response(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);

        return response.writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(message.getBytes())));
    }

}
