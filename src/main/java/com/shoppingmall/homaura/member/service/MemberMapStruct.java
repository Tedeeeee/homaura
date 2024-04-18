package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.vo.RequestMemberDto;
import com.shoppingmall.homaura.member.vo.ResponseMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapStruct {

    Member toMember(RequestMemberDto memberDto);

    ResponseMemberDto toMember(Member member);
}
