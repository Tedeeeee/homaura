package com.shoppingmall.homaura.security.utils;

import com.shoppingmall.homaura.member.entity.RefreshToken;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.member.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenUtil {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    public String createAccessToken(String memberUUID) {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(memberUUID))
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setExpiration(createAccessTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public String createRefreshToken() {
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(createRefreshTokenExpiredDate())
                .signWith(createSignature(), SignatureAlgorithm.HS256);
        return builder.compact();
    }

    // 캘린더를 사용해서 작성하는 만료 시간
    // accessToken 만료기간
    private static Date createAccessTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 50);
        return c.getTime();
    }

    // refreshToken 만료기간
    private static Date createRefreshTokenExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    // header 설정
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    // memberUUID 넣기
    private static Map<String, String> createClaims(String memberUUID) {
        Map<String, String> claims = new HashMap<>();

        claims.put("memberUUID", memberUUID);
        return claims;
    }

    // 보유하고 있는 키로 서명
    private Key createSignature() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 안에 있는 정보 가져오기
    private Claims getClaimsFormToken(String token) {
        Key key = createSignature();
        Claims body = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        if (body != null) {
            log.info("토큰 해석 성공: " + body);
            return body;
        } else {
            log.error("토큰 해석 실패");
            return null;
        }
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            if (claims != null) {
                log.info("expireTime : " + claims.getExpiration());
                isTokenExpired(claims.getExpiration());
                return true;
            }
            return false;
        } catch (JwtException exception) {
            log.error("토큰에 문제가 있습니다");
            throw new JwtException("토큰에 문제 발생");
        } catch (NullPointerException exception) {
            log.error("토큰이 존재하지 않습니다");
            throw new NullPointerException("토큰이 존재하지 않습니다");
        }
    }
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("AccessToken", accessToken);
        response.addHeader("RefreshToken", refreshToken);

        log.info("AccessToken, RefreshToken 데이터 전송 완료");
    }

    // refreshToken 저장하기
    public void insertRefreshToken(String memberUUID, String refreshTokenValue) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .memberUUID(memberUUID)
                .refreshToken(refreshTokenValue)
                .build();
        refreshTokenRepository.save(newRefreshToken);
    }

    public String getMemberUUIDFromToken(String token) {
        Claims claims = getClaimsFormToken(token);

        if (claims == null) {
            throw new RuntimeException("토큰 정보가 존재하지 않습니다");
        }

        return claims.get("memberUUID").toString();
    }

    private boolean isTokenExpired(Date expiration){
        boolean expire = expiration.before(new Date());
        if (expire) {
            throw new RuntimeException("만료된 JWT 토큰입니다, 다시 로그인 해주세요");
        }
        return true;
    }
}
