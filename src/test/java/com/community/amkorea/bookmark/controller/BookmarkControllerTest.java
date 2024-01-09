package com.community.amkorea.bookmark.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.community.amkorea.bookmark.dto.BookmarkRequest;
import com.community.amkorea.bookmark.dto.BookmarkResponse;
import com.community.amkorea.bookmark.service.BookmarkServiceImpl;
import com.community.amkorea.global.Util.Jwt.JwtAuthenticationFilter;
import com.community.amkorea.global.Util.Jwt.TokenProvider;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

  @MockBean
  BookmarkServiceImpl bookmarkService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  TokenProvider tokenProvider;

  @MockBean
  JwtAuthenticationFilter jwtAuthenticationFilter;

  @Autowired
  WebApplicationContext webApplicationContext;

  @MockBean
  private MemberRepository memberRepository;

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
  @WithMockUser
  @DisplayName("북마크 생성 성공")
  void createBookmark_success() throws Exception {
    //given
    BookmarkRequest bookmarkRequest = new BookmarkRequest("80");

    given(bookmarkService.createBookmark(anyString(), any()))
        .willReturn(BookmarkResponse.builder()
            .teamName("팀이름")
            .country("한국")
            .coach("코치")
            .build());

    //when
    //then
    mockMvc.perform(post("/api/bookmark")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(bookmarkRequest)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @WithMockUser
  @DisplayName("북마크 조회 성공")
  void findBookmark() throws Exception {
    //given
    given(bookmarkService.findBookmark(anyString()))
        .willReturn(List.of(BookmarkResponse.builder()
            .teamName("팀이름")
            .country("한국")
            .coach("코치")
            .build()));

    //when
    //then
    mockMvc.perform(get("/api/bookmark")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }
}