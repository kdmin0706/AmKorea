package com.community.amkorea.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Test
  @DisplayName("해당 이메일 존재 - 회원가입 실패")
  void signUp_DuplicateUser() {
    //given
    SignUpDto user = SignUpDto.builder()
        .email("test@test.com")
        .password(passwordEncoder.encode("12345678"))
        .nickname("김철수")
        .phoneNumber("010-1234-5678")
        .build();

    given(memberRepository.existsByEmail(any()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> authService.signUp(user));

    //then
    assertEquals(ErrorCode.DUPLICATE_USER, customException.getErrorCode());
  }
}