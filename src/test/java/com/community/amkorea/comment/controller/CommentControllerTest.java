package com.community.amkorea.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.comment.dto.CommentRequest;
import com.community.amkorea.comment.dto.CommentResponse;
import com.community.amkorea.comment.repository.CommentRepository;
import com.community.amkorea.comment.service.impl.CommentServiceImpl;
import com.community.amkorea.global.Util.Jwt.JwtAuthenticationFilter;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
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

@WebMvcTest(CommentController.class)
class CommentControllerTest {

  @MockBean
  private CommentServiceImpl commentService;
  @MockBean
  private CommentRepository commentRepository;
  @MockBean
  private TokenProvider tokenProvider;
  @MockBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  @Autowired
  WebApplicationContext webApplicationContext;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;


  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }

  @Test
  @DisplayName("댓글 작성 성공")
  @WithMockUser
  void createComment_Success() throws Exception {
    //given
    given(commentService.createComment(anyString(), any()))
        .willReturn(CommentResponse.builder()
            .memberId(1L)
            .postId(1L)
            .content("댓글 씁니다")
            .build());

    //when
    //then
    mockMvc.perform(post("/api/comment")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(new CommentRequest(1L, "댓글 씁니다")))
        .with(csrf()))
        .andExpect(status().isCreated())
        .andDo(print());

  }

}