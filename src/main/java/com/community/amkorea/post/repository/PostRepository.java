package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> , CustomPostRepository{

  @Query("SELECT p FROM Post p WHERE p.content LIKE %:content%"
      + " ORDER BY p.id DESC, p.views DESC, p.likeCount DESC")
  Page<Post> findAllByContentContaining(String content, Pageable pageable);
}


