package com.community.amkorea.comment.repository;

import com.community.amkorea.comment.entity.CommentLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
  boolean existsCommentLikeByCommentIdAndMember_Email(Long commentId, String username);
  Optional<CommentLike> findCommentLikesByCommentIdAndMember_Email(Long commentId, String username);
}
