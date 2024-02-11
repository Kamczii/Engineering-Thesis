package org.once_a_day.messenger.messenger_app.strategy.process_message.strategy.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.database.model.ChatMessageType;
import org.once_a_day.database.model.Match;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.file_storage.file_storage_api.kafka.request.UploadAudioMessageRequestDTO;
import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;
import org.once_a_day.messenger.messenger_app.producers.MessengerKafkaProducer;
import org.once_a_day.messenger.messenger_app.repository.MatchRepository;
import org.once_a_day.messenger.messenger_app.repository.MessageRepository;
import org.once_a_day.messenger.messenger_app.repository.MessageTypeRepository;
import org.once_a_day.messenger.messenger_app.repository.UserRepository;
import org.once_a_day.messenger.messenger_app.strategy.process_message.strategy.ProcessMessageStrategy;
import org.once_a_day.messenger.messenger_common.enums.MessageType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessAudioMessageImpl implements ProcessMessageStrategy {
    private final MessengerKafkaProducer messengerKafkaProducer;
    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final MessageTypeRepository messageTypeRepository;

    @Override
    public void process(final Long matchId, final InputChatMessageDTO message) {
        final var entity = createAudioMessage(matchId, message);
        final var save = save(entity);
        final var processAudioRequest = buildProcessAudioRequest(message, save);
        messengerKafkaProducer.send(processAudioRequest);
    }

    private static UploadAudioMessageRequestDTO buildProcessAudioRequest(final InputChatMessageDTO message, final ChatMessage save) {
        return UploadAudioMessageRequestDTO.builder()
                .messageId(save.getId())
                .content(message.getContent())
                .userId(message.getSenderId())
                .build();
    }

    private ChatMessage save(final ChatMessage entity) {
        return messageRepository.save(entity);
    }

    private ChatMessage createAudioMessage(Long matchId, InputChatMessageDTO message) {
        final var match = fetchMatch(matchId);
        final User sender = fetchUserById(message.getSenderId());
        final User receiver = fetchUserById(message.getReceiverId());
        final ChatMessageType chatMessageType = fetchType(message.getType());
        return ChatMessage.builder()
                .match(match)
                .sender(sender)
                .receiver(receiver)
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
