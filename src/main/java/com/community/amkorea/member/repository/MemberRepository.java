package com.community.amkorea.member.repository;

import com.community.amkorea.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  boolean existsByEmail(String email);

  Optional<Member> findByEmail(String email);
}
