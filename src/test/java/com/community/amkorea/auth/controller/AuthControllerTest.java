package com.community.amkorea.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.auth.dto.SignInDto;
import com.community.amkorea.auth.dto.SignUpDto;
import com.community.amkorea.auth.service.AuthService;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.global.Util.Mail.service.MailService;
import com.community.amkorea.global.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

  @MockBean
  private AuthService authService;

  @MockBean
  private MailService mailService;

  @MockBean
  private RedisService redisService;

  @MockBean
  private TokenProvider tokenProvider;

  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService, mailService)).build();
  }

  @Test
  @DisplayName("회원가입 성공")
  void success_signUp() throws Exception {
    //given
    SignUpDto user = SignUpDto.builder()
        .email("test@test.com")
        .password("12345678")
        .nickname("김철수")
        .phoneNumber("010-1234-5678")
        .build();
    given(authService.signUp(any())).willReturn(user);

    //when
    //then
    mockMvc.perform(post("/api/auth/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value("test@test.com"))
        .andExpect(jsonPath("$.password").value("12345678"))
        .andDo(print());
  }


  @Test
  @DisplayName("로그인 성공")
  void success_signIn() throws Exception {
    //given
    SignInDto signInDto = SignInDto.builder()
        .email("test@test.com")
        .password("12345")
        .build();

    //when
    //then
    mockMvc.perform(post("/api/auth/signIn")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signInDto))
            .with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }
}