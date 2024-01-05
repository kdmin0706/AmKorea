package com.community.amkorea.post.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostCategoryRequest;
import com.community.amkorea.post.dto.PostCategoryResponse;
import com.community.amkorea.post.entity.PostCategory;
import com.community.amkorea.post.repository.PostCategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class PostCategoryServiceImplTest {

  @InjectMocks
  private PostCategoryServiceImpl postCategoryService;

  @Mock
  private PostCategoryRepository postCategoryRepository;

  @Mock
  private MemberRepository memberRepository;

  @Test
  @DisplayName("게시글 카테고리 생성 성공")
  void createCategory_success() {
    //given
    Member member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .build();

    PostCategoryRequest request = getPostCategoryRequest();

    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    given(postCategoryRepository.save(any()))
        .willReturn(PostCategory.builder()
            .id(1L)
            .member(member)
            .name("레알")
            .build());

    //when
    PostCategoryResponse category
        = postCategoryService.createCategory("test@test.com", request);

    //then
    Assertions.assertEquals(request.name(), category.name());
  }

  private static PostCategoryRequest getPostCategoryRequest() {
    return PostCategoryRequest.builder()
        .name("레알")
        .build();
  }

  @Test
  @DisplayName("게시글 카테고리 생성 실패 - 해당 유저 없음")
  void createCategory_failUserNotFound() {
    //given
    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.empty());

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> postCategoryService.createCategory("test@test.com", getPostCategoryRequest()));

    //then
    Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, customException.getErrorCode());
  }

}