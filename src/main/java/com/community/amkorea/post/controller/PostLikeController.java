package com.community.amkorea.post.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {
  private final PostLikeService postLikeService;

  @PostMapping("/post/like")
  public ResponseEntity<?> like(@RequestParam(name = "id") Long postId, @LoginUser String username) {
    return ResponseEntity.ok(postLikeService.like(postId, username));
  }

  @PostMapping("/post/unlike")
  public ResponseEntity<?> unLike(@RequestParam(name = "id") Long postId, @LoginUser String username) {
    return ResponseEntity.ok(postLikeService.unLike(postId, username));
  }
}
