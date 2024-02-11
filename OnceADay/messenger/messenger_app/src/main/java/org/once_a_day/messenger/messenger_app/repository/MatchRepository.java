package org.once_a_day.messenger.messenger_app.repository;

import org.once_a_day.database.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Optional<Match> findByIdAndUserOneIdAndUserTwoId(Long matchId, Long oneId, Long twoId);
}
