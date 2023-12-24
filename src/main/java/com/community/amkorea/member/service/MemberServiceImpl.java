package com.community.amkorea.member.service;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.dto.MemberDto;
import com.community.amkorea.member.dto.UpdateMemberDto;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import java.util.Objects;
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
  public UpdateMemberDto updateMember(UpdateMemberDto updateMemberDto, String email) {
    Member member = getMember(email);
    if (!Objects.equals(member.getPassword(), updateMemberDto.getPassword())) {
      member.setPassword(passwordEncoder.encode(updateMemberDto.getPassword()));
    }
    if (!Objects.equals(member.getNickname(), updateMemberDto.getNickname())) {
      member.setNickname(updateMemberDto.getNickname());
    }
    if (!Objects.equals(member.getPhoneNumber(), updateMemberDto.getPhoneNumber())) {
      member.setPhoneNumber(updateMemberDto.getPhoneNumber());
    }

    return UpdateMemberDto.from(MemberDto.fromEntity(memberRepository.save(member)));
  }

  private Member getMember(String username) {
    return memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

}
