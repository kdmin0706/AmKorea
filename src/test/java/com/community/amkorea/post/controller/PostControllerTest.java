package com.community.amkorea.post.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.global.service.RedisService;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.service.PostLikeService;
import com.community.amkorea.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
  }

  @Test
  @WithMockUser
  @DisplayName("게시글 생성 성공")
  void success_createPost() throws Exception {
    //given
    PostResponse responseDto = getResponse();

    PostRequest request
        = new PostRequest("첫 게시물입니다.", "첫 내용입니다.", "카테고리입니다.");

    given(postService.createPost(any(), anyString(), anyList())).willReturn(responseDto);

    MockMultipartFile json = new MockMultipartFile("request", "json",
        "application/json", objectMapper.writeValueAsBytes(request));

    MockMultipartFile images = new MockMultipartFile("images", "image.png",
        "png", new FileInputStream("src/test/java/resources/image.png"));

    //when
    //then
    mockMvc.perform(multipart(HttpMethod.POST, "/api/post")
                    .file(json)
                    .file(images)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
  }

  @Test
  @DisplayName("게시글 목록 조회")
  void findPosts_success() throws Exception {
    //given
    given(postService.findPosts(any()))
        .willReturn(new PageImpl<>(List.of(getResponse())));

    //when
    //then
    mockMvc.perform(get("/api/post/searchAll")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].title").value("첫 게시물입니다."))
        .andExpect(jsonPath("$.content[0].content").value("첫 내용입니다."))
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