package com.example.userservice.security.filter;

import com.example.userservice.member.entity.Member;
import com.example.userservice.member.entity.RefreshToken;
import com.example.userservice.member.repository.MemberRepository;
import com.example.userservice.member.repository.RefreshTokenRepository;
import com.example.userservice.security.utils.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private static final List<Pattern> EXCLUDE_PATTERNS = Collections.unmodifiableList(Arrays.asList(
//            Pattern.compile("/login"),
//            Pattern.compile("/validationEmail"),
//            Pattern.compile("/checkCode"),
//            Pattern.compile("/signup"),
//            Pattern.compile("/checkNickname"),
//            Pattern.compile("/products"),
//            Pattern.compile("/products/.*"),
//            Pattern.compile("/products/.*/search")
            Pattern.compile("/*")
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RefreshToken refreshToken = extractRefreshToken(request);

        if (refreshToken == null) {
            throw new RuntimeException("로그인을 해주세요");
        }

        checkAccessToken(request, response, filterChain);
    }

    private RefreshToken extractRefreshToken(HttpServletRequest request) {
        return refreshTokenRepository.findByRefreshToken(request.getHeader("AuthorizationRefresh"));
    }

    private String extractAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) {
            throw new RuntimeException("로그인을 진행해주세요");
        }

        String accessToken = request.getHeader("Authorization");
        return accessToken.replace("Bearer ", "");
    }

    public void checkAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);
        String memberUUID = "";
        if (tokenUtil.isValidToken(accessToken)) {
            memberUUID = tokenUtil.getMemberUUIDFromToken(accessToken);
        }

        Member member = memberRepository.findByMemberUUID(memberUUID);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }

        saveAuthentication(member);
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        UserDetails userDetailsMember = User.builder()
                .username(member.getMemberUUID())
                .password(member.getPassword())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsMember, null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return EXCLUDE_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(servletPath).matches());
    }
}
