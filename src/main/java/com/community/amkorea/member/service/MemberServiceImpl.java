package com.community.amkorea.member.service;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.dto.MemberDto;
import com.community.amkorea.member.dto.UpdateMemberResponse;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public MemberDto findMember(String username) {
    return MemberDto.fromEntity(getMember(username));
  }

  @Override
  @Transactional
  public UpdateMemberResponse updateMember(UpdateMemberResponse updateMemberDto, String username) {
    Member member = getMember(username);

    member.setPassword(passwordEncoder.encode(updateMemberDto.getPassword()));
    member.setNickname(updateMemberDto.getNickname());
    member.setPhoneNumber(updateMemberDto.getPhoneNumber());

    return UpdateMemberResponse.from(MemberDto.fromEntity(member));
  }

  private Member getMember(String username) {
    return memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

}
