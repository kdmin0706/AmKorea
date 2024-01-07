package com.community.amkorea.comment.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.comment.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentLikeController {
  private final CommentLikeService commentLikeService;

  @PostMapping("/like")
  public ResponseEntity<?> like(@RequestParam(name = "id") Long commentId, @LoginUser String username) {
    return ResponseEntity.ok(commentLikeService.like(commentId, username));
  }

  @PostMapping("/unlike")
  public ResponseEntity<?> unLike(@RequestParam(name = "id") Long commentId, @LoginUser String username) {
    return ResponseEntity.ok(commentLikeService.unLike(commentId, username));
  }
}
