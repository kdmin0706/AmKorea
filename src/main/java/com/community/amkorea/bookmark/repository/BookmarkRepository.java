package com.community.amkorea.bookmark.repository;

import com.community.amkorea.bookmark.entity.Bookmark;
import com.community.amkorea.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  boolean existsByTeamId(String teamId);

  List<Bookmark> findAllByMember(Member member);
}
