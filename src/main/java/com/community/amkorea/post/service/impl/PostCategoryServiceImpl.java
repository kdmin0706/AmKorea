package com.community.amkorea.post.service.impl;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostCategoryRequest;
import com.community.amkorea.post.dto.PostCategoryResponse;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.entity.PostCategory;
import com.community.amkorea.post.repository.PostCategoryRepository;
import com.community.amkorea.post.repository.PostRepository;
import com.community.amkorea.post.service.PostCategoryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

  private final MemberRepository memberRepository;
  private final PostCategoryRepository postCategoryRepository;
  private final PostRepository postRepository;

  @Override
  public PostCategoryResponse createCategory(String username, PostCategoryRequest request) {
    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    PostCategory postCategory = request.toEntity(member);
    return PostCategoryResponse.fromEntity(postCategoryRepository.save(postCategory));
  }

  @Override
  @Transactional
  public PostCategoryResponse updateCategory(Long id, String username, PostCategoryRequest request) {
    return memberRepository.findByEmail(username).map(e -> {
      PostCategory postCategory = postCategoryRepository.findById(id)
          .orElseThrow(() -> new CustomException(ErrorCode.POST_CATEGORY_NOT_FOUND));

      postCategory.changeCategoryName(request.name());
      return PostCategoryResponse.fromEntity(postCategory);

    }).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  @Transactional(readOnly = true)
  public List<PostResponse> getCategory(Long id) {
    postCategoryRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_CATEGORY_NOT_FOUND));

    return postRepository.findPostsByCategoryId(id).stream()
        .map(PostResponse::fromEntity).collect(Collectors.toList());
  }

}
