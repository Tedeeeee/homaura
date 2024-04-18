package com.shoppingmall.homaura.security.handler;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.security.utils.TokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String memberUUID = extractUsername(authentication);
        String accessToken = tokenUtil.createAccessToken(memberUUID);
        String refreshToken = tokenUtil.createRefreshToken();

        Member member = memberRepository.findByMemberUUID(memberUUID);

        if (member != null) {
            tokenUtil.insertRefreshToken(member.getMemberUUID(), refreshToken);
            tokenUtil.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        }

        log.info("로그인에 성공하였습니다. 이메일 : {}", memberUUID);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("로그인에 성공하였습니다. RefreshToken : {}", refreshToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
