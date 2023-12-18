package com.community.amkorea.auth.service;

import static com.community.amkorea.global.exception.ErrorCode.PASSWORD_NOT_MATCH;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @Override
  public SignUpDto signUp(SignUpDto request) {
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.DUPLICATE_USER);
    }

    Member savedMember = memberRepository.save(Member.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .phoneNumber(request.getPhoneNumber())
            .nickname(request.getNickname())
            .build());

    return SignUpDto.fromEntity(savedMember);
  }

  @Override
  public void signIn(SignInDto request) {
    Member member = memberRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new CustomException(PASSWORD_NOT_MATCH);
    }
  }
}
