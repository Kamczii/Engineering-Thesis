package org.once_a_day.messenger.messenger_app.repository;

import org.once_a_day.database.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findAllByMatchIdAndContentNotNullOrAttachmentNotNull(Long matchId, final Pageable pageable);
}
