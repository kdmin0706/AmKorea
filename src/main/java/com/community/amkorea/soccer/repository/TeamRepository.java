package com.community.amkorea.soccer.repository;

import com.community.amkorea.soccer.entity.Team;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

  Optional<Team> findByName(String name);

  @Query(" select distinct t.id from Team t ")
  List<String> findByAllId();
}
