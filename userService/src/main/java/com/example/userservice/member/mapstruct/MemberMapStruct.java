package com.example.userservice.member.mapstruct;

import com.example.userservice.member.dto.MemberDto;
import com.example.userservice.member.entity.Member;
import com.example.userservice.member.vo.RequestMember;
import com.example.userservice.member.vo.RequestUpdate;
import com.example.userservice.member.vo.ResponseMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapStruct {
    MemberDto changeMemberDto(RequestMember requestMember);
    MemberDto changeMemberDto(RequestUpdate requestUpdate);

    @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(com.example.userservice.member.entity.Role.USER)")
    @Mapping(target = "memberUUID", expression = "java(java.util.UUID.randomUUID().toString())")
    Member changeEntity(MemberDto memberDto);

    // 여기서
    MemberDto changeMemberDto(Member member);

    ResponseMember changeResponse(MemberDto memberDto);
}
