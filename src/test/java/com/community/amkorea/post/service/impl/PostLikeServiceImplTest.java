package com.community.amkorea.post.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostLikeResponse;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.entity.PostLike;
import com.community.amkorea.post.repository.PostLikeRepository;
import com.community.amkorea.post.repository.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {
  @InjectMocks
  PostLikeServiceImpl postLikeService;
  @Mock
  PostLikeRepository postLikeRepository;
  @Mock
  private PostRepository postRepository;
  @Mock
  private MemberRepository memberRepository;

  private Member member;
  private Post post;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .build();

    post = Post.builder()
        .id(1L)
        .title("제목")
        .content("내용")
        .views(1)
        .member(member)
        .build();
  }

  @Test
  @DisplayName("좋아요 성공")
  void like() {
    //given
    given(postLikeRepository.existsPostLikeByPostIdAndMember_Email(anyLong(), anyString()))
        .willReturn(false);

    given(postRepository.findById(anyLong()))
        .willReturn(Optional.of(post));

    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    given(postLikeRepository.save(any()))
        .willReturn(PostLike.builder()
            .post(post)
            .member(member)
            .build());

    //when
    PostLikeResponse liked = postLikeService.like(1L, "test@test.com");

    //then
    Assertions.assertTrue(liked.isLike());
  }

  @Test
  @DisplayName("좋아요 실패 - 현재 게시물은 좋아요가 이미 눌려있습니다")
  void like_FAIL_POST_ALREADY_LIKE() {
    //given
    given(postLikeRepository.existsPostLikeByPostIdAndMember_Email(anyLong(), anyString()))
        .willReturn(true);

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> postLikeService.like(1L, "test@test.com"));

    //then
    Assertions.assertEquals(ErrorCode.POST_ALREADY_LIKE, customException.getErrorCode());
  }

  @Test
  @DisplayName("좋아요 취소 실패 - 현재 게시물은 좋아요가 취소되어있습니다")
  void unLike_FAIL_POST_ALREADY_LIKE() {
    //given
    given(postLikeRepository.findByPostIdAndMember_Email(anyLong(), anyString()))
        .willReturn(Optional.empty());

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> postLikeService.unLike(1L, "test@test.com"));

    //then
    Assertions.assertEquals(ErrorCode.POST_ALREADY_UNLIKE, customException.getErrorCode());
  }
}