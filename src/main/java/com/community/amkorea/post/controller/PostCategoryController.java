package com.community.amkorea.post.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.post.dto.PostCategoryRequest;
import com.community.amkorea.post.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostCategoryController {

  private final PostCategoryService postCategoryService;

  @PostMapping("/category")
  public ResponseEntity<?> createCategory(@LoginUser String username,
                                          @RequestBody PostCategoryRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(postCategoryService.createCategory(username, request));
  }

  @PutMapping("/category")
  public ResponseEntity<?> updateCategory(@RequestParam("id") Long id, @LoginUser String username,
                                          @RequestBody PostCategoryRequest request) {
    return ResponseEntity.ok(postCategoryService.updateCategory(id, username, request));
  }

  @GetMapping("/category")
  public ResponseEntity<?> getCategory(@RequestParam("id") Long id) {
    return ResponseEntity.ok(postCategoryService.getCategory(id));
  }

}
