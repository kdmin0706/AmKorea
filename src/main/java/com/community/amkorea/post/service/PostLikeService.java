package com.community.amkorea.post.service;

import com.community.amkorea.post.dto.PostLikeResponse;

public interface PostLikeService {
  PostLikeResponse like(Long postId, String username);
  PostLikeResponse unLike(Long postId, String username);
}
