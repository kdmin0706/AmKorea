package com.community.amkorea.member.service;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.member.dto.MemberDto;
import com.community.amkorea.member.dto.UpdateMemberResponse;
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
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class MemberServiceImplTest {

  @InjectMocks
  private MemberServiceImpl memberService;

  @Mock
  private MemberRepository memberRepository;

  private Member member;

  @BeforeEach
  void setup() {
    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .nickname("김철수")
        .phoneNumber("010-1234-6789")
        .roleType(RoleType.USER)
        .build();
  }
  @Test
  @DisplayName("회원 정보 조회 완료")
  void success_findMember() {
    //given
    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    //when
    MemberDto memberDto = memberService.findMember("test@test.com");

    //then
    Assertions.assertEquals(member.getEmail(), memberDto.getEmail());
  }

  @Test
  @DisplayName("회원 정보 수정 완료")
  void success_updateMember() {
    //given
    UpdateMemberResponse update = UpdateMemberResponse.builder()
        .nickname("김영희")
        .phoneNumber("010-5555-6666")
        .password("12345")
        .build();

    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    //when
    UpdateMemberResponse memberDto = memberService.updateMember(update, "test@test.com");

    //then
    Assertions.assertEquals(update.getNickname(), memberDto.getNickname());
  }

}