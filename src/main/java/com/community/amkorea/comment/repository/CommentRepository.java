package com.community.amkorea.comment.repository;

import com.community.amkorea.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query(" SELECT c FROM Comment c LEFT JOIN Post p ON c.post.id = p.id WHERE c.post.id = :postId ")
  Page<Comment> findCommentsByPost(@Param("postId") Long postId, Pageable pageable);

}
