package org.once_a_day.messenger.messenger_app.repository;

import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.database.model.FileDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDetails, Long> {
}
