package org.once_a_day.messenger.messenger_app.strategy.process_message.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.database.model.ChatMessageType;
import org.once_a_day.database.model.Match;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;
import org.once_a_day.messenger.messenger_api.dto.ResponseChatMessageDTO;
import org.once_a_day.messenger.messenger_app.repository.MatchRepository;
import org.once_a_day.messenger.messenger_app.repository.MessageRepository;
import org.once_a_day.messenger.messenger_app.repository.MessageTypeRepository;
import org.once_a_day.messenger.messenger_app.repository.UserRepository;
import org.once_a_day.messenger.messenger_app.service.AuthorizationService;
import org.once_a_day.messenger.messenger_app.service.SendMessageService;
import org.once_a_day.messenger.messenger_app.strategy.process_message.strategy.ProcessMessageStrategy;
import org.once_a_day.messenger.messenger_common.enums.MessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessChatMessageImpl implements ProcessMessageStrategy {
    private final MatchRepository matchRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SendMessageService sendMessageService;
    private final MessageTypeRepository messageTypeRepository;

    @Override
    public void process(final Long matchId, final InputChatMessageDTO message) {
        final var entity = createRegularMessage(matchId, message);
        final var save = save(entity);
        sendMessageService.send(save.getId());
    }

    private ChatMessage save(final ChatMessage entity) {
        return messageRepository.save(entity);
    }

    private ChatMessage createRegularMessage(Long matchId, InputChatMessageDTO message) {
        final var match = fetchMatch(matchId);
        final User sender = fetchUserById(message.getSenderId());
        final User receiver = fetchUserById(message.getReceiverId());
        final ChatMessageType chatMessageType = fetchType(message.getType());
        return ChatMessage.builder()
                .match(match)
                .sender(sender)
                .receiver(receiver)
                .content(message.getContent())
                .type(chatMessageType)
                .build();
    }



    private Match fetchMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private User fetchUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private ChatMessageType fetchType(MessageType type) {
        return messageTypeRepository.findByType(type)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

}
