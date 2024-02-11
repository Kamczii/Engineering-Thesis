package org.once_a_day.messenger.messenger_app.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.messenger.messenger_api.dto.ResponseChatMessageDTO;
import org.once_a_day.messenger.messenger_app.mapper.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageMapper implements Mapper<ChatMessage, ResponseChatMessageDTO> {

    @Value("${org.once_a_day.streams.url}")
    private String streamsUrl;

    @Override
    public ResponseChatMessageDTO map(final ChatMessage chatMessage) {
        final var attachmentUrl = resolveAttachmentUrl(chatMessage);
        return ResponseChatMessageDTO.builder()
                .content(chatMessage.getContent())
                .type(chatMessage.getType().getType())
                .senderId(chatMessage.getSender().getId())
                .receiverId(chatMessage.getReceiver().getId())
                .createdTime(chatMessage.getCreatedTime())
                .sentTime(chatMessage.getSentTime())
                .id(chatMessage.getId())
                .attachmentUrl(attachmentUrl)
                .build();
    }

    private String resolveAttachmentUrl(final ChatMessage chatMessage) {
        return Optional.ofNullable(chatMessage.getAttachment())
                .map(FileDetails::getId)
                .map(this::resolveStreamUrl)
                .orElse(null);
    }

    private String resolveStreamUrl(final Long id) {
        return streamsUrl + "/audio/" + id;
    }
}
