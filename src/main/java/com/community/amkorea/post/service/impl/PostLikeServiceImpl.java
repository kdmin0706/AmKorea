package com.community.amkorea.post.service.impl;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.exception.ErrorCode;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostLikeResponse;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.entity.PostLike;
import com.community.amkorea.post.repository.PostLikeRepository;
import com.community.amkorea.post.repository.PostRepository;
import com.community.amkorea.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

  private final PostLikeRepository postLikeRepository;
  private final PostRepository postRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public PostLikeResponse like(Long postId, String username) {
    if (postLikeRepository.existsPostLikeByPostIdAndMember_Email(postId, username)) {
      throw new CustomException(ErrorCode.POST_ALREADY_LIKE);
    }

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    PostLike postLike = PostLike.builder().build();

    postLike.addMember(member);
    postLike.addPost(post);
    post.updateLikeCount();

    postLikeRepository.save(postLike);

    return PostLikeResponse.builder()
        .postId(post.getId())
        .username(member.getEmail())
        .isLike(true)
        .build();
  }

  @Override
  @Transactional
  public PostLikeResponse unLike(Long postId, String username) {
    return postLikeRepository.findByPostIdAndMember_Email(postId, username)
        .map(postLike -> {
          postLikeRepository.delete(postLike);
          postRepository.findById(postId).ifPresent(e -> {
                e.removePostLike(postLike);
                e.updateLikeCount();
              });

          return PostLikeResponse.builder()
              .postId(postId)
              .username(username)
              .isLike(false)
              .build();

        }).orElseThrow(() -> new CustomException(ErrorCode.POST_ALREADY_UNLIKE));
  }
}
