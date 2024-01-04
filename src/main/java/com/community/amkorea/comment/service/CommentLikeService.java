package com.community.amkorea.comment.service;

import com.community.amkorea.comment.dto.CommentLikeResponse;

public interface CommentLikeService {
  CommentLikeResponse like(Long commentId, String username);
  CommentLikeResponse unLike(Long commentId, String username);
}
