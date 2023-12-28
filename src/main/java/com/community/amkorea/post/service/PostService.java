package com.community.amkorea.post.service;

import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

  /**
   * 게시글 생성
   */
  PostResponse createPost(PostRequest requestDto, String username);

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
  Page<PostResponse> findPosts(Pageable pageable);

  /**
   * 제목이 포함된 키워드를 검색해서 게시글 찾기
   */
  Page<PostResponse> searchTitle(String title, Pageable pageable);

  /**
   * 내용 포함된 키워드 검색해서 게시글 찾기
   */
  Page<PostResponse> searchContent(String content, Pageable pageable);
}
