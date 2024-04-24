package com.shoppingmall.homaura.member.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPassword {
    private String email;

    @NotNull(message = "기존의 비밀번호를 작성해주세요")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{8,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 8글자 이상이어야 합니다")
    private String nowPassword;

    @NotNull(message = "새로운 비밀번호를 작성해주세요")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{8,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 8글자 이상이어야 합니다")
    private String newPassword;

    @NotNull(message = "새로운 비밀번호를 한 번 더 작성해주세요")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{8,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 8글자 이상이어야 합니다")
    private String rePassword;
}
