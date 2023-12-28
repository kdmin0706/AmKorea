package com.community.amkorea.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.community.amkorea.member.entity.Member;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.repository.PostRepository;
import com.community.amkorea.post.service.impl.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
class PostServiceImplTest {
  @InjectMocks
  private PostServiceImpl postService;

  @Mock
  private PostRepository postRepository;

  @Test
  @DisplayName("게시글 생성 성공")
  void createPost_success() {
    //given
    Member member = Member.builder()
        .id(1L)
        .email("test@test.com")
        .password("12345")
        .phoneNumber("010-1234-4564")
        .build();

    given(postRepository.save(any()))
        .willReturn(Post.builder()
            .id(1L)
            .title("제목")
            .content("내용")
            .viewNumber(1)
            .member(member)
            .build());

    PostRequest request = new PostRequest("제목", "내용");

    //when
    PostResponse response = postService.createPost(request, "test@test.com");

    //then
    assertThat(response.getContent()).isEqualTo(request.getContent());
    assertEquals(request.getTitle(), response.getTitle());
  }


}