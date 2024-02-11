package org.once_a_day.file_storage.file_storage_app.repository;

import org.once_a_day.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySsoId(UUID ssoId);
}
