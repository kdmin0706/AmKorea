package com.community.amkorea.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class AuthServiceImplTest {

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private TokenProvider tokenProvider;

  private Member member;

  @BeforeEach
  void setup() {
    member = Member.builder()
        .email("test@test.com")
        .password("12345")
        .roleType(RoleType.USER)
        .build();
  }


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

  @Test
  @DisplayName("회원가입 성공")
  void success_signUp() {
    //given
    SignUpDto signUpDto = SignUpDto.builder()
        .email("test@test.com")
        .password(passwordEncoder.encode("12345678"))
        .nickname("김철수")
        .phoneNumber("010-1234-5678")
        .build();

    given(memberRepository.save(any()))
        .willReturn(Member.builder()
            .email("test@test.com")
            .phoneNumber("010-1234-5678")
            .build());

    //when
    SignUpDto signed = authService.signUp(signUpDto);

    //then
    assertEquals("test@test.com", signed.getEmail());
    assertEquals("010-1234-5678", signed.getPhoneNumber());
  }

  @Test
  @DisplayName("로그인 실패 - 없는 이메일")
  void fail_Login_not_exist_email() {
    //given
    SignInDto signInDto = SignInDto.builder()
        .email("test@test.com")
        .password("12345")
        .build();

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> authService.signIn(signInDto));

    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, customException.getErrorCode());
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호 틀림")
  void fail_Login_password_not_match() {
    //given
    SignInDto signInDto = SignInDto.builder()
        .email("test@test.com")
        .password("12345")
        .build();

    doReturn(Optional.of(member)).when(memberRepository).findByEmail(anyString());

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> authService.signIn(signInDto));

    //then
    assertEquals(ErrorCode.PASSWORD_NOT_MATCH, customException.getErrorCode());
  }

  @Test
  @DisplayName("로그인 실패 - 이메일 인증 미완료")
  void fail_Login_emailVerify() {
    //given
    SignInDto signInDto = SignInDto.builder()
        .email("test@test.com")
        .password("12345")
        .build();

    doReturn(Optional.of(member)).when(memberRepository).findByEmail(anyString());
    doReturn(true).when(passwordEncoder).matches(any(),any());

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> authService.signIn(signInDto));

    //then
    assertEquals(ErrorCode.EMAIL_NOT_VERITY, customException.getErrorCode());
  }

}