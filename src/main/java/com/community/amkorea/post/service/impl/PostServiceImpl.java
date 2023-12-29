package com.community.amkorea.post.service.impl;

import static com.community.amkorea.global.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.POST_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.POST_NOT_MINE;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.repository.PostRepository;
import com.community.amkorea.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public PostResponse createPost(PostRequest requestDto, String username) {
    Post post = requestDto.toEntity();
    Member member = getMember(username);
    member.addPost(post);
    return PostResponse.fromEntity(postRepository.save(post));
  }

  @Override
  @Transactional
  public void delete(Long id, String username) {
    Post post = getPost(id);
    Member member = getMember(username);

    validationPost(post, member);

    member.removePost(post);
    postRepository.delete(post);
  }

  @Override
  @Transactional
  public PostResponse updatePost(Long id, PostRequest requestDto, String username) {
    Post post = getPost(id);
    Member member = getMember(username);

    validationPost(post, member);

    post.setTitle(requestDto.getTitle());
    post.setContent(requestDto.getContent());

    return PostResponse.fromEntity(post);
  }

  private Member getMember(String username) {
    return memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));
  }

  private void validationPost(Post post, Member member) {
    //본인의 게시물이 맞는지 확인
    if (!post.getMember().getEmail().equals(member.getEmail())) {
      throw new CustomException(POST_NOT_MINE);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> findPosts(Pageable pageable) {
    return postRepository.findAll(pageable).map(PostResponse::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Slice<PostResponse> searchTitle(Long id, String name, Pageable pageable) {
    return postRepository.searchByTitle(id, name, pageable).map(PostResponse::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> searchContent(String content, Pageable pageable) {
    return postRepository.findAllByContentContaining(content, pageable).map(PostResponse::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponse findPost(Long id) {
    return PostResponse.fromEntity(getPost(id));
  }

  private Post getPost(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
  }

}
