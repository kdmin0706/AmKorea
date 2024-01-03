package com.community.amkorea.post.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebMvcTest(PostController.class)
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

  @Autowired
  WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private Member member;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();

    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .roleType(RoleType.USER)
        .build();

    memberRepository.save(member);
  }

  @AfterEach
  void clear() {
    memberRepository.delete(member);
  }

  @Test
  @WithMockUser
  @DisplayName("게시글 생성 성공")
  void success_createPost() throws Exception {
    //given
    PostResponse responseDto = getResponse();

    given(postService.createPost(any(), anyString())).willReturn(responseDto);

    //when
    //then
    mockMvc.perform(post("/api/post")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            new PostRequest("첫 게시물입니다.", "첫 내용입니다.", "카테고리입니다.")))
        .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andDo(print());
  }


  private static PostResponse getResponse() {
    return PostResponse.builder()
        .id(1L)
        .username("test@test.com")
        .title("첫 게시물입니다.")
        .content("첫 내용입니다.")
        .views(1)
        .likeCount(1)
        .build();
  }
}