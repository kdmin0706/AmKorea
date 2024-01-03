package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

public interface CustomPostRepository {
  Slice<Post> searchByTitle(Long postId, String title, Pageable pageable);

  @Transactional
  void UpdateViews(Long id, int views);
}
