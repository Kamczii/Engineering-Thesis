package org.once_a_day.messenger.messenger_app.listeners;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_api.kafka.MessengerKafkaTopics;
import org.once_a_day.messenger.messenger_api.kafka.response.AudioMessageUploadedDTO;
import org.once_a_day.messenger.messenger_app.repository.FileRepository;
import org.once_a_day.messenger.messenger_app.repository.MessageRepository;
import org.once_a_day.messenger.messenger_app.service.SendMessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AudioUploadListener {
    private final SendMessageService sendMessageService;
    private final MessageRepository messageRepository;
    private final FileRepository fileRepository;

    @KafkaListener(topics = MessengerKafkaTopics.AUDIO_MESSAGE_UPLOADED_KAFKA_TOPIC, groupId = "messenger-group")
    public void onUploadAudioRequest(AudioMessageUploadedDTO message) {
        final var chatMessage = fetchMessage(message.getMessageId());
        final var fileDetails = fetchFile(message.getFileId());
        chatMessage.setAttachment(fileDetails);
        messageRepository.save(chatMessage);
        sendMessageService.send(chatMessage.getId());
    }

    private ChatMessage fetchMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private FileDetails fetchFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND, FileDetails.class.getSimpleName()));
    }
}
