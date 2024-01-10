package com.community.amkorea.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.global.Util.Jwt.JwtAuthenticationFilter;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.member.dto.MemberDto;
import com.community.amkorea.member.dto.UpdateMemberResponse;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.service.MemberServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

  @MockBean
  MemberServiceImpl memberService;
  @MockBean
  TokenProvider tokenProvider;
  @MockBean
  JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  WebApplicationContext webApplicationContext;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  @WithMockUser
  @DisplayName("멤버 정보 조회")
  void findMemberInfo() throws Exception {
    //given
    given(memberService.findMember(anyString()))
        .willReturn(MemberDto.builder()
            .id(1L)
            .email("test@test.com")
            .nickname("닉네임")
            .password("12345")
            .emailAuth(true)
            .phoneNumber("010-1234-5678")
            .roleType(RoleType.USER.getCode())
            .build());

    //when
    //then
    mockMvc.perform(get("/api/member")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser
  @DisplayName("멤버 정보 수정")
  void updateMember() throws Exception {
    //given
    given(memberService.updateMember(any(),anyString()))
        .willReturn(UpdateMemberResponse.builder()
            .nickname("닉네임")
            .password("12345")
            .phoneNumber("010-1234-5678")
            .build());

    //when
    //then
    mockMvc.perform(put("/api/member")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(
                new UpdateMemberResponse("닉", "123", "010-1234-5678"))))
        .andExpect(jsonPath("$.nickname").value("닉네임"))
        .andExpect(jsonPath("$.password").value("12345"))
        .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
        .andExpect(status().isOk())
        .andDo(print());
  }
}