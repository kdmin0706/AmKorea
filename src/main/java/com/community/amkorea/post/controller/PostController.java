package com.community.amkorea.post.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
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

  @GetMapping("/post/detail/{id}")
  public ResponseEntity<?> readPost(@PathVariable Long id, HttpServletRequest request) {
    return ResponseEntity.ok(postService.readPost(id, request));
  }

  @GetMapping("/post/search/{id}")
  public ResponseEntity<?> searchPost(@PathVariable Long id) {
    return ResponseEntity.ok(postService.findPost(id));
  }

  @GetMapping("/post/searchAll")
  public ResponseEntity<?> searchAllPost(@PageableDefault(sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.findPosts(pageable));
  }

  @GetMapping("/post/search/title")
  public ResponseEntity<?> searchTitle(@RequestParam("id") Long id,
      @RequestParam("name") String name,
      @PageableDefault(sort="createdAt", direction= Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchTitle(id, name, pageable));
  }

  @GetMapping("/post/search/content")
  public ResponseEntity<?> searchContent(@RequestParam("name") String name,
      @PageableDefault(sort="createdAt", direction= Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(postService.searchContent(name, pageable));
  }
}
