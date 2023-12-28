package com.community.amkorea.post.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.service.PostLikeService;
import com.community.amkorea.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final PostLikeService postLikeService;

  @PostMapping("/post")
  public ResponseEntity<?> create(@Valid @RequestBody PostRequest requestDto,
      @LoginUser String username) {
    return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(requestDto, username));
  }

  @DeleteMapping("/post/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id, @LoginUser String username) {
    postService.delete(id, username);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/post/{id}")
  public ResponseEntity<?> updatePost(@PathVariable Long id,
      @Valid @RequestBody PostRequest requestDto,
      @LoginUser String username) {
    return ResponseEntity.ok(postService.updatePost(id, requestDto, username));
  }

  @GetMapping("/post/{id}")
  public ResponseEntity<?> findPost(@PathVariable Long id) {
    return ResponseEntity.ok(postService.findPost(id));
  }

  @GetMapping("/posts")
  public ResponseEntity<?> findAllPost(@PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.findPosts(pageable));
  }

  @GetMapping("/post/search/title/{title}")
  public ResponseEntity<?> searchTitle(@PathVariable String title,
      @PageableDefault(size = 15, sort="createdAt", direction= Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchTitle(title, pageable));
  }

  @GetMapping("/post/search/content/{content}")
  public ResponseEntity<?> searchContent(@PathVariable String content,
      @PageableDefault(size = 15, sort="createdAt", direction= Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchContent(content, pageable));
  }

  @PostMapping("/post/like")
  public ResponseEntity<?> like(@RequestParam(name = "id") Long postId, @LoginUser String username) {
    return ResponseEntity.ok(postLikeService.like(postId, username));
  }

  @PostMapping("/post/unlike")
  public ResponseEntity<?> unLike(@RequestParam(name = "id") Long postId, @LoginUser String username) {
    return ResponseEntity.ok(postLikeService.unLike(postId, username));
  }

}
