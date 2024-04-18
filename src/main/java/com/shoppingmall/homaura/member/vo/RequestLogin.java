package com.shoppingmall.homaura.member.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestLogin {

    @NotNull(message = "이메일을 작성해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotNull(message = "비밀번호를 작성해주세요")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{8,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 8글자 이상이어야 합니다")
    private String password;
}
