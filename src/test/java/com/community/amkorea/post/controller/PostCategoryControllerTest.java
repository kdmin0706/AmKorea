package com.community.amkorea.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.global.Util.Jwt.JwtAuthenticationFilter;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.mock.CustomMockUser;
import com.community.amkorea.post.dto.PostCategoryRequest;
import com.community.amkorea.post.dto.PostCategoryResponse;
import com.community.amkorea.post.service.impl.PostCategoryServiceImpl;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(PostCategoryController.class)
class PostCategoryControllerTest {

  @MockBean
  private PostCategoryServiceImpl postCategoryService;

  @MockBean
  private TokenProvider tokenProvider;

  @MockBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MemberRepository memberRepository;

  @Autowired
  WebApplicationContext webApplicationContext;

  @Mock
  private Member member;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .roleType(RoleType.USER)
        .build();

    memberRepository.save(member);

    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .build();
  }

  @AfterEach
  void clear() {
    memberRepository.delete(member);
  }

  @Test
  @CustomMockUser
  @DisplayName("카테고리 생성 완료")
  void create_category() throws Exception {
    //given
    given(postCategoryService.createCategory(anyString(), any()))
        .willReturn(PostCategoryResponse.builder()
                  .id(1L)
                  .memberId(member.getId())
                  .name("토트넘")
                  .build());

    //when
    //then
    mockMvc.perform(post("/api/post/category")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            new PostCategoryRequest("토트넘")))
            .header("Authorization", "bcdedit")
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("토트넘"))
        .andDo(print());
  }
}