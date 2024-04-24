package com.example.userservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.userservice.member.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UsernamePasswordAuthenticationToken authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Member user = objectMapper.readValue(request.getInputStream(), Member.class);

            return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
