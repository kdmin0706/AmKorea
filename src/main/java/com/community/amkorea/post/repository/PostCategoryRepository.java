package com.community.amkorea.post.repository;

import com.community.amkorea.post.entity.PostCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
  Optional<PostCategory> findByName(String name);
}
