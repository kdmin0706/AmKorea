package com.community.amkorea.bookmark.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.bookmark.dto.BookmarkRequest;
import com.community.amkorea.bookmark.dto.BookmarkResponse;
import com.community.amkorea.bookmark.entity.Bookmark;
import com.community.amkorea.bookmark.repository.BookmarkRepository;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.soccer.entity.Team;
import com.community.amkorea.soccer.repository.TeamRepository;
import java.util.List;
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

@Transactional
@ExtendWith(MockitoExtension.class)
class BookmarkServiceImplTest {

  @InjectMocks
  BookmarkServiceImpl bookmarkService;

  @Mock
  BookmarkRepository bookmarkRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  TeamRepository teamRepository;

  private Member member;
  private Team team;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .build();

    team = Team.builder()
        .id("80")
        .name("팀 이름")
        .coach("코치")
        .country("한국")
        .venue("상암경기장")
        .build();
  }

  @Test
  @DisplayName("북마크 생성 성공")
  void createBookmark_success() {
    //given
    BookmarkRequest request = new BookmarkRequest("80");

    given(bookmarkRepository.existsByTeamId(request.getTeamId()))
        .willReturn(false);

    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    given(teamRepository.findById(any()))
        .willReturn(Optional.ofNullable(team));

    given(bookmarkRepository.save(any()))
        .willReturn(Bookmark.builder()
            .id(1L)
            .member(member)
            .team(team)
            .build());

    //when
    BookmarkResponse response = bookmarkService.createBookmark("사람", request);

    //then
    Assertions.assertEquals(request.getTeamId(), response.getTeamId());
  }


  @Test
  @DisplayName("북마크 생성 실패 - 이미 존재하는 북마크")
  void createBookmark_failExistTeamId() {
    //given
    BookmarkRequest request = new BookmarkRequest("80");

    given(bookmarkRepository.existsByTeamId(request.getTeamId()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> bookmarkService.createBookmark("사람", request));

    //then
    Assertions.assertEquals(ErrorCode.ALREADY_REGISTERED, customException.getErrorCode());
  }

  @Test
  @DisplayName("북마크 조회 성공")
  void findBookmark_success() {
    //given
    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    given(bookmarkRepository.findAllByMember(member))
        .willReturn(List.of(Bookmark.builder()
            .id(1L)
            .member(member)
            .team(team)
            .build()));

    //when
    List<BookmarkResponse> bookmark = bookmarkService.findBookmark(member.getEmail());

    //then
    Assertions.assertEquals(team.getId(), bookmark.get(0).getTeamId());
    Assertions.assertEquals(team.getName(), bookmark.get(0).getTeamName());
  }
}