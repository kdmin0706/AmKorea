package com.community.amkorea.post.service;

import com.community.amkorea.post.dto.PostCategoryRequest;
import com.community.amkorea.post.dto.PostCategoryResponse;
import com.community.amkorea.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCategoryService {
  PostCategoryResponse createCategory(String username, PostCategoryRequest request);

  PostCategoryResponse updateCategory(Long id, String username, PostCategoryRequest request);

  Page<PostResponse> getCategory(Long id, Pageable pageable);
}
