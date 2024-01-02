package com.community.amkorea.post.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.auth.config.LoginUserArgumentResolver;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.global.service.RedisService;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.service.PostLikeService;
import com.community.amkorea.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;


@WebMvcTest(PostController.class)
@Transactional
class PostControllerTest {

  @MockBean
  private PostService postService;

  @MockBean
  private PostLikeService postLikeService;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TokenProvider tokenProvider;

  @MockBean
  private RedisService redisService;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private LoginUserArgumentResolver loginUserArgumentResolver;

  private MockMvc mockMvc;
  @BeforeEach
  void init() {
    mockMvc = MockMvcBuilders.standaloneSetup(new PostController(postService, postLikeService))
        .setCustomArgumentResolvers(loginUserArgumentResolver)
        .build();
  }

  @Test
  @DisplayName("게시글 생성 성공")
  void success_createPost() throws Exception {
    //given
    Member member = getMember();

    PostResponse responseDto = PostResponse.builder()
        .id(1L)
        .username("test@test.com")
        .title("첫 게시물입니다.")
        .content("첫 내용입니다.")
        .build();

    given(loginUserArgumentResolver.supportsParameter(any())).willReturn(true);
    given(loginUserArgumentResolver.resolveArgument(any(),any(),any(),any())).willReturn(member);

    given(postService.createPost(any(), anyString())).willReturn(responseDto);

    //when
    //then
    mockMvc.perform(post("/api/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            new PostRequest("첫 게시물입니다.", "첫 내용입니다.")))
        .with(csrf()))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  private static Member getMember() {
    return Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .roleType(RoleType.USER)
        .build();
  }
}