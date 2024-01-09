package com.community.amkorea.soccer.repository;

import com.community.amkorea.soccer.entity.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
  @Query(" select distinct p.id from Player p ")
  List<String> findByAllId();
}
