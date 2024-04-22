package com.shoppingmall.homaura.member.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdate {
    private String email;

    @NotNull(message = "전화번호를 - 없이 작성해주세요")
    @Pattern(regexp = "^\\d{10,}$", message = "전화번호를 - 없이 10자리 이상의 숫자로 작성해주세요")
    private String phone;

    @NotNull(message = "정확한 주소를 작성해주세요")
    @Pattern(regexp = "^.{10,}$", message = "주소는 최소 10글자 이상이어야 합니다")
    private String address;
}
