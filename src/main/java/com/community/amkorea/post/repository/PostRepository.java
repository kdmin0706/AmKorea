package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> , CustomPostRepository{
  Page<Post> findAllByContentContaining(String content, Pageable pageable);
}


