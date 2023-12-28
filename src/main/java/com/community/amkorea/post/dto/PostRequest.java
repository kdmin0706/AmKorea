package com.community.amkorea.post.dto;

import com.community.amkorea.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostRequest {
  @NotBlank
  private String title;

  @NotBlank
  @Size(max = 500)
  private String content;

  public Post toEntity() {
    return Post.builder()
        .title(this.title)
        .content(this.content)
        .build();
  }
}