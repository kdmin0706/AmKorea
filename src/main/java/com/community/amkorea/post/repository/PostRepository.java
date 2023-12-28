package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findAllByTitleContaining(String title, Pageable pageable);

  @Query(" select p from Post p where p.content like %:content% ")
  Page<Post> findByContent(@Param("content") String content, Pageable pageable);

}


