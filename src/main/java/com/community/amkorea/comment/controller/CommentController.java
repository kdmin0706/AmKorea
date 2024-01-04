package com.community.amkorea.comment.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.comment.dto.CommentRequest;
import com.community.amkorea.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<?> createComment(@LoginUser String username,
                                          @Valid @RequestBody CommentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentService.createComment(username, request));
  }

  @PutMapping
  public ResponseEntity<?> updateComment(@RequestParam("id") Long id, @LoginUser String username,
                                          @Valid @RequestBody CommentRequest request) {
    return ResponseEntity.ok(commentService.updateComment(id, username, request));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteComment(@RequestParam("id") Long id, @LoginUser String username) {
    commentService.deleteComment(id, username);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<?> GetComment(@RequestParam("post") Long id,
                                      @PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(commentService.getCommentList(id, pageable));
  }

}
