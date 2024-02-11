package org.once_a_day.sso.repository;

import org.once_a_day.database.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
