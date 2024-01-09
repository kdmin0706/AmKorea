package com.community.amkorea.bookmark.controller;

import com.community.amkorea.auth.config.LoginUser;
import com.community.amkorea.bookmark.dto.BookmarkRequest;
import com.community.amkorea.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
  private final BookmarkService bookmarkService;

  @PostMapping
  public ResponseEntity<?> createBookmark(@LoginUser String username,
                                          @Valid @RequestBody BookmarkRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(bookmarkService.createBookmark(username, request));
  }

  @GetMapping
  public ResponseEntity<?> findBookmark(@LoginUser String username) {
    return ResponseEntity.ok(bookmarkService.findBookmark(username));
  }

  @DeleteMapping
  public ResponseEntity<?> deleteBookmark(@LoginUser String username, @RequestParam("id") Long id) {
    bookmarkService.deleteBookmark(username, id);
    return ResponseEntity.noContent().build();
  }

}
