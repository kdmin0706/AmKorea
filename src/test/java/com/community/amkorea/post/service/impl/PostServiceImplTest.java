package com.community.amkorea.post.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.aws.dto.S3ImageDto;
import com.community.amkorea.aws.service.AWSS3Service;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.entity.PostCategory;
import com.community.amkorea.post.repository.PostCategoryRepository;
import com.community.amkorea.post.repository.PostRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class PostServiceImplTest {
  @InjectMocks
  private PostServiceImpl postService;
  @Mock
  AWSS3Service awsS3Service;
  @Mock
  private PostRepository postRepository;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PostCategoryRepository postCategoryRepository;

  private Member member;

  @BeforeEach
  void setUp() {
    member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .build();
  }

  @Test
  @DisplayName("게시글 생성 성공")
  void createPost_success() throws IOException {
    //given
    PostRequest request = new PostRequest("제목", "내용", "카테고리이름");

    PostCategory postCategory = PostCategory.builder()
        .id(1L)
        .member(member)
        .build();

    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    given(postCategoryRepository.findByName("카테고리이름"))
        .willReturn(Optional.ofNullable(postCategory));

    given(awsS3Service.uploadFile(any()))
        .willReturn(new S3ImageDto("q1w2e3r4", "", 1L));

    given(postRepository.save(any()))
        .willReturn(Post.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .views(1)
            .member(member)
            .postCategory(postCategory)
            .build());

    MockMultipartFile images = new MockMultipartFile("images", "image.png",
        "png", new FileInputStream("src/test/java/resources/image.png"));

    //when
    PostResponse response = postService.createPost(request, "test@test.com", List.of(images));

    //then
    assertThat(response.getContent()).isEqualTo(request.getContent());
    assertEquals(request.getTitle(), response.getTitle());
  }

  @Test
  @DisplayName("게시글 생성 실패 - 카테고리 이름 없음")
  void createPost_fail() throws IOException {
    //given
    PostRequest request = new PostRequest("제목", "내용", "카테고리이름");

    given(memberRepository.findByEmail(anyString()))
        .willReturn(Optional.of(member));

    given(postCategoryRepository.findByName("카테고리이름"))
        .willReturn(Optional.empty());

    given(awsS3Service.uploadFile(any()))
        .willReturn(new S3ImageDto("q1w2e3r4", "a1a2a3a4a5", 1L));

    MockMultipartFile images = new MockMultipartFile("images", "image.png",
        "png", new FileInputStream("src/test/java/resources/image.png"));

    //when
    CustomException customException = Assertions.assertThrows(CustomException.class,
        () -> postService.createPost(request, "test@test.com", List.of(images)));

    //then
    Assertions.assertEquals(ErrorCode.POST_CATEGORY_NOT_FOUND, customException.getErrorCode());
  }


}