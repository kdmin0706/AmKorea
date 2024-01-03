package com.community.amkorea.post.service.impl;

import static com.community.amkorea.global.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.POST_CATEGORY_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.POST_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.POST_NOT_MINE;

import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.global.service.RedisService;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.post.dto.PostRequest;
import com.community.amkorea.post.dto.PostResponse;
import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.repository.PostCategoryRepository;
import com.community.amkorea.post.repository.PostRepository;
import com.community.amkorea.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final MemberRepository memberRepository;
  private final PostCategoryRepository postCategoryRepository;

  private final RedisService redisService;
  private static final String VIEW_HASH_KEY = "postViews";

  @Override
  @Transactional
  public PostResponse createPost(PostRequest requestDto, String username) {
    Post post = requestDto.toEntity();
    Member member = getMember(username);
    member.addPost(post);

    postCategoryRepository.findByName(requestDto.getCategory())
        .ifPresentOrElse(post::addCategory,
            () -> { throw new CustomException(POST_CATEGORY_NOT_FOUND);});

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
  public Page<PostResponse> searchContent(String name, Pageable pageable) {
    return postRepository.findAllByContentContaining(name, pageable).map(PostResponse::fromEntity);
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponse findPost(Long id) {
    return PostResponse.fromEntity(getPost(id));
  }

  @Override
  @Transactional
  public PostResponse readPost(Long id, HttpServletRequest request) {
    Post post = getPost(id);

    HttpSession session = request.getSession();

    //클라이언트의 세션에서 중복 조회 여부 확인
    Boolean hasRead = (Boolean) session.getAttribute("readPost: " + id);

    if (hasRead == null || !hasRead) {

      //중복 조회 여부를 세션에 저장
      session.setAttribute("readPost: " + id, true);

      //데이터 증가
      redisService.increaseHashData(VIEW_HASH_KEY, id.toString());

    } else {
      log.info("중복 요청 발생으로 인한 조회수 미반영");
    }

    return PostResponse.fromEntity(post);
  }

  private Post getPost(Long id) {
    return postRepository.findById(id).orElseThrow(() -> new CustomException(POST_NOT_FOUND));
  }

  @Scheduled(cron = "${spring.scheduler.refresh-time}")
  public void updateViewCountToDB() {
    Map<Object, Object> map = redisService.hasHashKeys(VIEW_HASH_KEY);

    for (Map.Entry<Object, Object> entry : map.entrySet()) {
      Long postId = Long.parseLong(entry.getKey().toString());
      int views = Integer.parseInt(entry.getValue().toString());

      //DB에 데이터 반영
      postRepository.updateViews(postId, views);

      //데이터 적용 후 삭제
      redisService.deleteHashKey(VIEW_HASH_KEY, postId.toString());
    }
  }

}
