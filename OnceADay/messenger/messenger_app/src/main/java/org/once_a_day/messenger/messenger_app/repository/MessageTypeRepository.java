package org.once_a_day.messenger.messenger_app.repository;

import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.database.model.ChatMessageType;
import org.once_a_day.messenger.messenger_common.enums.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageTypeRepository extends JpaRepository<ChatMessageType, Long> {
    Optional<ChatMessageType> findByType(MessageType type);
}
