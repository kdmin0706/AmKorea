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
  @NotBlank(message = "제목을 입력해주세요")
  private String title;

  @NotBlank(message = "내용을 입력해주세요")
  @Size(max = 500)
  private String content;

  @NotBlank(message = "카테고리를 입력해주세요")
  private String category;

  public Post toEntity() {
    return Post.builder()
        .title(this.title)
        .content(this.content)
        .build();
  }
}