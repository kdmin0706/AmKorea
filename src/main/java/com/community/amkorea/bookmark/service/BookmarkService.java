package com.community.amkorea.bookmark.service;

import com.community.amkorea.bookmark.dto.BookmarkRequest;
import com.community.amkorea.bookmark.dto.BookmarkResponse;
import java.util.List;

public interface BookmarkService {
  BookmarkResponse createBookmark(String username, BookmarkRequest request);

  List<BookmarkResponse> findBookmark(String username);

  void deleteBookmark(String username, Long id);
}
