package com.shoppingmall.homaura.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentMemberUUID() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null) {
            throw new RuntimeException("로그인을 다시 확인해주세요");
        }
        return authentication.getName();
    }
}
