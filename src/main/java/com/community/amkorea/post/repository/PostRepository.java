package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> , CustomPostRepository{

  @Query("SELECT p FROM Post p WHERE p.content LIKE %:content%"
      + " ORDER BY p.id DESC, p.views DESC, p.likeCount DESC")
  Page<Post> findAllByContentContaining(String content, Pageable pageable);

  @Query("SELECT p FROM Post p LEFT JOIN p.postCategory c WHERE c.id = :categoryId")
  List<Post> findPostsByCategoryId(@Param("categoryId") Long categoryId);
}


