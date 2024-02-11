package org.once_a_day.messenger.messenger_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_api.dto.ResponseChatMessageDTO;
import org.once_a_day.messenger.messenger_app.mapper.impl.MessageMapper;
import org.once_a_day.messenger.messenger_app.repository.MessageRepository;
import org.once_a_day.messenger.messenger_app.service.SendMessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public void send(Long messageId) {
        final var chatMessage = fetchMessage(messageId);
        final var dto = this.map(chatMessage);
        final var matchId = chatMessage.getMatch().getId();
        sendToSender(matchId, dto);
        sendToReceiver(matchId, dto);
    }

    private ResponseChatMessageDTO map(ChatMessage chatMessage) {
        return messageMapper.map(chatMessage);
    }

    private ChatMessage fetchMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private void sendToReceiver(final Long matchId, final ResponseChatMessageDTO messageDTO) {
        final Map<String, Object> headers = Map.of("messageId", messageDTO.getId(), "receiverId", messageDTO.getReceiverId());
        simpMessagingTemplate.convertAndSendToUser(messageDTO.getReceiverId().toString(), "/match/"+ matchId, messageDTO, headers);
    }

    private void sendToSender(final Long matchId, final ResponseChatMessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSendToUser(messageDTO.getSenderId().toString(), "/match/"+ matchId, messageDTO);
    }

}
