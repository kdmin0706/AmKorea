package com.community.amkorea.comment.service.impl;

import static com.community.amkorea.global.exception.ErrorCode.WRITE_NOT_YOURSELF;
import static com.community.amkorea.member.entity.enums.RoleType.ADMIN;

import com.community.amkorea.comment.dto.CommentRequest;
import com.community.amkorea.comment.dto.CommentResponse;
import com.community.amkorea.comment.entity.Comment;
import com.community.amkorea.comment.repository.CommentRepository;
import com.community.amkorea.comment.service.CommentService;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final MemberRepository memberRepository;
  private final PostRepository postRepository;

  @Override
  public CommentResponse createComment(String username, CommentRequest request) {
    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Post post = postRepository.findById(request.postId())
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    Comment comment = Comment.builder()
        .content(request.content())
        .build();

    comment.addPostAndMember(post, member);
    return CommentResponse.fromEntity(commentRepository.save(comment));
  }

  @Override
  @Transactional
  public CommentResponse updateComment(Long id, String username, CommentRequest request) {
    postRepository.findById(request.postId())
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    //본인의 댓글이 맞는지 확인
    if (comment.getMember().getEmail().equals(username)) {
      throw new CustomException(WRITE_NOT_YOURSELF);
    }

    comment.changeContent(request.content());
    return CommentResponse.fromEntity(comment);
  }

  @Override
  @Transactional
  public void deleteComment(Long id, String username) {
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    validationDeleteComment(comment, username);
    commentRepository.delete(comment);
  }

  /**
   * 관리자는 모든 게시물 삭제 가능
   * 유저의 경우에는 본인 게시물만 삭제 가능
   */
  private void validationDeleteComment(Comment comment, String username) {
    if (comment.getMember().getRoleType() != ADMIN
        && !comment.getMember().getEmail().equals(username)) {
      throw new CustomException(WRITE_NOT_YOURSELF);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CommentResponse> getCommentList(Long postId, Pageable pageable) {
    return commentRepository.findCommentsByPost(postId, pageable).map(CommentResponse::fromEntity);
  }

}
