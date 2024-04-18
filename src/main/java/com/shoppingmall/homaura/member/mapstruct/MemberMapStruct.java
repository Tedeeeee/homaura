package com.shoppingmall.homaura.member.mapstruct;

import com.shoppingmall.homaura.member.dto.MemberDto;
import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.vo.RequestMember;
import com.shoppingmall.homaura.member.vo.ResponseMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapStruct {
    MemberDto changeMemberDto(RequestMember requestMember);

    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(com.shoppingmall.homaura.member.entity.Role.USER)")
    Member changeEntity(MemberDto memberDto);

    MemberDto changeMemberDto(Member member);

    ResponseMember changeResponse(MemberDto memberDto);
}