package org.once_a_day.messenger.messenger_app.repository;

import org.once_a_day.database.model.Match;
import org.once_a_day.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySsoId(UUID ssoId);
}
