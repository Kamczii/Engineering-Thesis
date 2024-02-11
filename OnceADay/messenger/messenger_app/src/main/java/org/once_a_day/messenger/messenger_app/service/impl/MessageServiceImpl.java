package org.once_a_day.messenger.messenger_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;
import org.once_a_day.messenger.messenger_api.dto.ResponseChatMessageDTO;
import org.once_a_day.messenger.messenger_app.mapper.impl.MessageMapper;
import org.once_a_day.messenger.messenger_app.strategy.process_message.context.ProcessMessageContext;
import org.once_a_day.messenger.messenger_app.repository.MessageRepository;
import org.once_a_day.messenger.messenger_app.service.AuthorizationService;
import org.once_a_day.messenger.messenger_app.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final AuthorizationService authorizationService;
    private final ProcessMessageContext processMessageContext;
    private final MessageMapper messageMapper;

    @Override
    public void process(final String sessionId, final Long matchId, InputChatMessageDTO message) {
        processMessageContext.resolve(message.getType())
                .process(matchId, message);
    }

    @Override
    public void process(final Long matchId, InputChatMessageDTO message) {
        processMessageContext.resolve(message.getType())
                .process(matchId, message);
    }

    @Override
    public Page<ResponseChatMessageDTO> getMessages(final Long matchId, final Pageable pageable) {
        final var page = messageRepository.findAllByMatchIdAndContentNotNullOrAttachmentNotNull(matchId, pageable);
        final Long currentUserId = authorizationService.getCurrentUserId();
        page.stream()
                .filter(msg -> msg.getSentTime() == null)
                .filter(msg -> msg.getReceiver().getId().equals(currentUserId))
                .map(this::markAsSent).forEach(messageRepository::save);
        return page.map(messageMapper::map);
    }

    private ChatMessage markAsSent(ChatMessage message) {
        message.setSentTime(LocalDateTime.now());
        return message;
    }
}
