package com.community.amkorea.comment.service.impl;

import com.community.amkorea.comment.dto.CommentLikeResponse;
import com.community.amkorea.comment.entity.Comment;
import com.community.amkorea.comment.entity.CommentLike;
import com.community.amkorea.comment.repository.CommentLikeRepository;
import com.community.amkorea.comment.repository.CommentRepository;
import com.community.amkorea.comment.service.CommentLikeService;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private final CommentLikeRepository commentLikeRepository;

  @Override
  @Transactional
  public CommentLikeResponse like(Long commentId, String username) {
    if (commentLikeRepository.existsCommentLikeByCommentIdAndMember_Email(commentId, username)) {
      throw new CustomException(ErrorCode.COMMENT_ALREADY_LIKE);
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    CommentLike commentLike = CommentLike.builder().build();

    commentLike.addComment(comment);
    commentLike.addMember(member);
    comment.updateLikeCount();

    commentLikeRepository.save(commentLike);

    return CommentLikeResponse.builder()
        .commentId(commentId)
        .username(username)
        .isLike(true)
        .build();
  }

  @Override
  @Transactional
  public CommentLikeResponse unLike(Long commentId, String username) {
    return commentLikeRepository.findCommentLikesByCommentIdAndMember_Email(commentId, username)
        .map(like -> {
          commentLikeRepository.delete(like);
          commentRepository.findById(commentId).ifPresent(e -> {
            e.removeCommentLike(like);
            e.updateLikeCount();
          });

          return CommentLikeResponse.builder()
              .commentId(commentId)
              .username(username)
              .isLike(false)
              .build();

        }).orElseThrow(() -> new CustomException(ErrorCode.COMMENT_ALREADY_UNLIKE));
  }
}
