package com.community.amkorea.post.repository.impl;

import static com.community.amkorea.post.entity.QPost.post;

import com.community.amkorea.post.entity.Post;
import com.community.amkorea.post.repository.CustomPostRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final EntityManager entityManager;

  @Override
  public Slice<Post> searchByTitle(Long postId, String title, Pageable pageable) {
    List<Post> postList = jpaQueryFactory.selectFrom(post)
        .where(
            ltPostId(postId), //첫번째 페이지 조회 시 ltPostId == null 처리
            post.title.contains(title)
        )
        .orderBy(
            post.id.desc(),
            post.views.desc(),
            post.likeCount.desc()
        )
        .limit(pageable.getPageSize() + 1)
        .fetch();

    // 무한 스크롤 처리
    return checkLastPage(pageable, postList);
  }

  @Override
  public void UpdateViews(Long id, int views) {
    jpaQueryFactory.update(post)
        .set(post.views, views)
        .where(post.id.eq(id).and(post.views.ne(views)))
        .execute();

    entityManager.flush();
    entityManager.clear();
  }

  /**
   * No-Offset 방식 처리 메서드
   */
  private BooleanExpression ltPostId(Long postId) {
    if (postId == null || postId == 0L) {
      return null;   //BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다.
    }
    return post.id.lt(postId);
  }

  /**
   * 무한 스크롤 방식을 처리하는 메서드
   */
  private Slice<Post> checkLastPage(Pageable pageable, List<Post> results) {

    boolean hasNext = false;

    // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
    if (results.size() > pageable.getPageSize()) {
      hasNext = true;
      results.remove(pageable.getPageSize());
    }

    return new SliceImpl<>(results, pageable, hasNext);
  }

}
