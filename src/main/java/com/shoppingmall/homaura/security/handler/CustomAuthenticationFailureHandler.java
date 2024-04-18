package com.shoppingmall.homaura.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        JSONObject jsonObject;
        String failMsg = "";

        if (exception instanceof AuthenticationServiceException) {
            failMsg = "로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof BadCredentialsException){
            failMsg = "비밀번호가 치하지 않습니다.";
        } else if (exception instanceof LockedException) {
            failMsg = "사용자의 계정은 잠겼습니다.";
        } else if (exception instanceof DisabledException){
            failMsg = "사용자의 계정이 비활성화 되어있습니다.";
        } else if (exception instanceof AccountExpiredException) {
            failMsg = "사용자의 계정이 만료되었습니다.";
        } else if (exception instanceof CredentialsExpiredException){
            failMsg = "해당 비밀번호는 변경되었습니다.";
        }

        log.debug(failMsg);

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("memberInfo", null);
        resultMap.put("resultCode", 9999);
        resultMap.put("failMsg", failMsg);
        jsonObject = new JSONObject(resultMap);

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}

