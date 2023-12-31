package com.community.amkorea.post.service;

import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {

  /**
   * 게시글 생성
   */
  PostResponse createPost(PostRequest requestDto, String username, List<MultipartFile> files);

  /**
   * 게시글 삭제
   */
  void delete(Long id, String username);

  /**
   * 게시글 수정
   */
  PostResponse updatePost(Long id, PostRequest requestDto, String username);

  /**
   * 단일 게시글 검색
   */
  PostResponse findPost(Long id);

  /**
   * 게시글 조회
   */
  PostResponse readPost(Long id, HttpServletRequest request);

  /**
   * 게시글 조회
   */
  Page<PostResponse> findPosts(Pageable pageable);

  /**
   * 제목이 포함된 키워드를 검색해서 게시글 찾기
   */
  Slice<PostResponse> searchTitle(Long id, String name, Pageable pageable);

  /**
   * 내용 포함된 키워드 검색해서 게시글 찾기
   */
  Page<PostResponse> searchContent(String name, Pageable pageable);
}
