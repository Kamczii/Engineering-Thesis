package org.once_a_day.file_storage.file_storage_app.listeners;

import lombok.RequiredArgsConstructor;
import org.once_a_day.file_storage.file_storage_api.kafka.FileStorageKafkaTopics;
import org.once_a_day.file_storage.file_storage_api.kafka.request.UploadAudioMessageRequestDTO;
import org.once_a_day.file_storage.file_storage_app.service.AudioService;
import org.once_a_day.messenger.messenger_api.kafka.MessengerKafkaTopics;
import org.once_a_day.messenger.messenger_api.kafka.response.AudioMessageUploadedDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class AudioUploadListener {
    private final AudioService audioService;

    @KafkaListener(topics = FileStorageKafkaTopics.AUDIO_MESSAGE_UPLOAD_REQUEST_KAFKA_TOPIC, groupId = "file-storage-group")
    public void onUploadAudioRequest(UploadAudioMessageRequestDTO message) {
        audioService.process(message.getUserId(), message.getMessageId(), message.getContent(), UUID.randomUUID().toString() + ".mp3");
    }
}
