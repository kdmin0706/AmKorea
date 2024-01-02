package com.community.amkorea.post.dto;

import com.community.amkorea.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
  private Long id;
  private String title;
  private String content;
  private String username;

  public static PostResponse fromEntity(Post post) {
    return PostResponse.builder()
        .id(post.getId())
        .username(post.getMember().getEmail())
        .title(post.getTitle())
        .content(post.getContent())
        .build();
  }
}
