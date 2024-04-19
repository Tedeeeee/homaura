package com.shoppingmall.homaura.member.mapstruct;

import com.shoppingmall.homaura.member.dto.MemberDto;
import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.vo.RequestMember;
import com.shoppingmall.homaura.member.vo.RequestUpdate;
import com.shoppingmall.homaura.member.vo.ResponseMember;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapStruct {
    MemberDto changeMemberDto(RequestMember requestMember);
    MemberDto changeMemberDto(RequestUpdate requestUpdate);

    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(com.shoppingmall.homaura.member.entity.Role.USER)")
    @Mapping(target = "memberUUID", expression = "java(java.util.UUID.randomUUID().toString())")
    Member changeEntity(MemberDto memberDto);

    MemberDto changeMemberDto(Member member);

    ResponseMember changeResponse(MemberDto memberDto);
}
