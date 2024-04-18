package com.shoppingmall.homaura.security.provider;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.entity.RefreshToken;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.member.repository.RefreshTokenRepository;
import com.shoppingmall.homaura.security.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailService customUserDetailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails user = customUserDetailService.loadUserByUsername(username);
        Member member = memberRepository.findByMemberUUID(user.getUsername());
        RefreshToken byMemberUUId = refreshTokenRepository.findByMemberUUID(member.getMemberUUID());

        if (byMemberUUId != null) {
            throw new RuntimeException("이미 접속중인 사용자입니다");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        return new UsernamePasswordAuthenticationToken(user, null, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
