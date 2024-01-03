package com.community.amkorea.post.dto;

import com.community.amkorea.post.entity.PostCategory;
import lombok.Builder;

@Builder
public record PostCategoryResponse(
    Long id,
    Long memberId,
    String name
) {

  public static PostCategoryResponse fromEntity(PostCategory postCategory) {
    return PostCategoryResponse.builder()
        .id(postCategory.getId())
        .memberId(postCategory.getMember().getId())
        .name(postCategory.getName())
        .build();
  }
}
