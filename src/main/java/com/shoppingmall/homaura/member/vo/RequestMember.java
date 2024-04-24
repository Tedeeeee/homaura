package com.shoppingmall.homaura.member.vo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RequestMember {

    @NotNull(message = "이메일을 작성해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "올바른 이메일 형식이 아닙니다")
    private String email;

    private String emailCode;

    @NotNull(message = "비밀번호를 작성해주세요")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^*+=-]).{8,}$", message = "비밀번호는 대문자와 지정된 특수문자를 최소 하나씩 포함하고, 8글자 이상이어야 합니다")
    private String password;

    @NotNull(message = "이름을 작성해주세요")
    @Size(min = 2, message = "이름을 작성해주세요")
    private String name;

    @NotNull(message = "닉네임을 작성해주세요")
    @Size(min = 2, message = "닉네임을 작성해주세요")
    private String nickname;

    private boolean nicknameVerified;

    @NotNull(message = "전화번호를 - 없이 작성해주세요")
    @Pattern(regexp = "^\\d{10,}$", message = "전화번호를 - 없이 10자리 이상의 숫자로 작성해주세요")
    private String phone;

    @NotNull(message = "정확한 주소를 작성해주세요")
    @Pattern(regexp = "^.{10,}$", message = "주소는 최소 10글자 이상이어야 합니다")
    private String address;
}
