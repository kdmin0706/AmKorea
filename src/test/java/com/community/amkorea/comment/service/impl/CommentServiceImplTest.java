package com.community.amkorea.comment.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.comment.dto.CommentRequest;
import com.community.amkorea.comment.dto.CommentResponse;
import com.community.amkorea.comment.entity.Comment;
import com.community.amkorea.comment.repository.CommentRepository;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.entity.enums.RoleType;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  @InjectMocks
  CommentServiceImpl commentService;

  @Mock
  CommentRepository commentRepository;
  @Mock
  MemberRepository memberRepository;
  @Mock
  PostRepository postRepository;

  private Member member;
  private Post post;
  @BeforeEach
  void setup() {
    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .nickname("김철수")
        .phoneNumber("010-1234-6789")
        .roleType(RoleType.USER)
        .build();

    post = Post.builder()
        .id(1L)
        .content("포스트 내용")
        .title("포스트 제목")
        .build();
  }

  @Test
  @DisplayName("댓글 작성 성공")
  void createComment_success() {
    //given
    CommentRequest request = new CommentRequest(1L, "내용");

    given(memberRepository.findByEmail(any()))
        .willReturn(Optional.of(member));

    given(postRepository.findById(anyLong()))
        .willReturn(Optional.of(post));

    given(commentRepository.save(any()))
        .willReturn(Comment.builder()
            .id(1L)
            .content("내용")
            .member(member)
            .post(post)
            .build());

    //when
    CommentResponse comment = commentService.createComment(member.getEmail(), request);

    //then
    Assertions.assertEquals(request.content(), comment.getContent());
  }

  @Test
  @DisplayName("댓글 조회 실패 - 댓글 없음")
  void comment_NotFound() {
    //given
    CommentRequest request = new CommentRequest(1L, "내용");

    given(postRepository.findById(any()))
        .willReturn(Optional.of(post));
    given(commentRepository.findById(any()))
        .willReturn(Optional.empty());
    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> commentService.updateComment(1L, "test@test.com", request));

    //then
    Assertions.assertEquals(ErrorCode.COMMENT_NOT_FOUND, customException.getErrorCode());
  }

  @Test
  @DisplayName("댓글 조회 실패 - 본인 게시물 X")
  void comment_WRITE_NOT_YOURSELF() {
    //given
    CommentRequest request = new CommentRequest(1L, "내용");

    Comment comment = Comment.builder()
        .id(1L)
        .post(post)
        .member(member)
        .build();

    given(postRepository.findById(any()))
        .willReturn(Optional.of(post));

    given(commentRepository.findById(any()))
        .willReturn(Optional.of(comment));

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> commentService.updateComment(1L, "test@test.com", request));

    //then
    Assertions.assertEquals(ErrorCode.WRITE_NOT_YOURSELF, customException.getErrorCode());
  }

}