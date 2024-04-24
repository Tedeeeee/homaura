package com.shoppingmall.homaura.member.service;

import com.shoppingmall.homaura.member.dto.MemberDto;
import com.shoppingmall.homaura.member.entity.Member;
import com.shoppingmall.homaura.member.entity.RefreshToken;
import com.shoppingmall.homaura.member.mapstruct.MemberMapStruct;
import com.shoppingmall.homaura.member.repository.MemberRepository;
import com.shoppingmall.homaura.member.repository.RefreshTokenRepository;
import com.shoppingmall.homaura.member.vo.ProductInfo;
import com.shoppingmall.homaura.member.vo.RequestPassword;
import com.shoppingmall.homaura.member.vo.ResponseMember;
import com.shoppingmall.homaura.security.utils.SecurityUtil;
import com.shoppingmall.homaura.wishlist.entity.WishList;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapStruct memberMapStruct;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String checkNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new RuntimeException("중복된 닉네임 입니다");
        }
        return "사용 가능한 닉네임입니다";
    }

    @Override
    public String createMember(MemberDto memberDto, HttpSession session) {
        String findEmail = (String) session.getAttribute(memberDto.getEmailCode());

        if (!findEmail.equals(memberDto.getEmail()) || findEmail.equals("")) {
            throw new RuntimeException("이메일 인증을 진행해주세요");
        }

        if (!memberDto.isNicknameVerified()) {
            throw new RuntimeException("닉네임 중복 확인을 해주세요");
        }
        String encodePwd = bCryptPasswordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodePwd);

        Member member = memberMapStruct.changeEntity(memberDto);

        try {
            memberRepository.save(member);
            session.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "회원 가입을 축하드립니다";
    }

    @Override
    public ResponseMember updateMember(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail());

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }
        member.changePhone(memberDto.getPhone());
        member.changeAddress(memberDto.getAddress());
        member.updateTime(LocalDateTime.now());

        MemberDto newMemberDto = memberMapStruct.changeMemberDto(memberRepository.save(member));

        return memberMapStruct.changeResponse(newMemberDto);
    }

    @Override
    public String updatePassword(RequestPassword requestPassword) {
        Member member = memberRepository.findByEmail(requestPassword.getEmail());
        if (!bCryptPasswordEncoder.matches(requestPassword.getNowPassword(), member.getPassword())) {
            throw new RuntimeException("기존 비밀번호와 일치하지 않습니다");
        }

        if (!requestPassword.getNewPassword().equals(requestPassword.getRePassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        member.changePassword(bCryptPasswordEncoder.encode(requestPassword.getNewPassword()));
        member.updateTime(LocalDateTime.now());
        memberRepository.save(member);

        RefreshToken refreshToken = refreshTokenRepository.findByMemberUUID(member.getMemberUUID());
        refreshTokenRepository.deleteById(refreshToken.getId());

        return "변경된 비밀번호로 다시 로그인해주세요";
    }

    @Override
    public String logout() {
        String memberUUID = SecurityUtil.getCurrentMemberUUID();
        Member member = memberRepository.findByMemberUUID(memberUUID);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByMemberUUID(member.getMemberUUID());

        if (refreshToken == null) {
            throw new RuntimeException("로그인 기록이 존재하지 않습니다");
        }

        refreshTokenRepository.deleteById(refreshToken.getId());
        return "로그아웃에 성공하였습니다";
    }

    @Override
    public MemberDto getUser() {
        String memberUUID = SecurityUtil.getCurrentMemberUUID();
        Member member = memberRepository.findByMemberUUID(memberUUID);

        if (member == null) {
            throw new RuntimeException("존재하지 않는 회원입니다");
        }

        List<WishList> wishLists = member.getWishLists();
        List<ProductInfo> productInfoList = new ArrayList<>();
        for (WishList wishList : wishLists) {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setProduct(wishList.getProduct());
            productInfo.setUnitCount(wishList.getUnitCount());

            productInfoList.add(productInfo);
        }

        MemberDto memberDto = memberMapStruct.changeMemberDto(member);
        memberDto.setWishLists(productInfoList);

        // 여기서 회원이 주문한 상품을 묶어서 전달해준다.
        return memberDto;
    }
}
