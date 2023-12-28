package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  boolean existsPostLikeByPostIdAndMember_Email(Long postId, String username);
  Optional<PostLike> findByPostIdAndMember_Email(Long postId, String username);
}
