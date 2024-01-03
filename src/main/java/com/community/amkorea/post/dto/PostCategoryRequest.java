package com.community.amkorea.post.dto;

import com.community.amkorea.member.entity.Member;
import com.community.amkorea.post.entity.PostCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PostCategoryRequest(@NotBlank String name) {
  public PostCategory toEntity(Member member) {
    return PostCategory.builder()
        .name(this.name)
        .member(member)
        .build();
  }
}
