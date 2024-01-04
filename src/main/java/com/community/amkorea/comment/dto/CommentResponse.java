package com.community.amkorea.comment.dto;

import com.community.amkorea.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {
  private Long memberId;
  private Long postId;
  private String content;

  public static CommentResponse fromEntity(Comment comment) {
    return CommentResponse.builder()
        .memberId(comment.getMember().getId())
        .postId(comment.getPost().getId())
        .content(comment.getContent())
        .build();
  }
}
