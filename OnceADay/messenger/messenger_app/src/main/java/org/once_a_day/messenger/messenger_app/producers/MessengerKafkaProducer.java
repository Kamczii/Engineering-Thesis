package org.once_a_day.messenger.messenger_app.producers;

import lombok.RequiredArgsConstructor;
import org.once_a_day.file_storage.file_storage_api.kafka.FileStorageKafkaTopics;
import org.once_a_day.file_storage.file_storage_api.kafka.request.UploadAudioMessageRequestDTO;
import org.once_a_day.messenger.messenger_api.kafka.MessengerKafkaTopics;
import org.once_a_day.messenger.messenger_api.kafka.response.AudioMessageUploadedDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessengerKafkaProducer {
    private final KafkaTemplate<String, Object> uploadFileKafkaTemplate;

    public void send(UploadAudioMessageRequestDTO request) {
        uploadFileKafkaTemplate.send(FileStorageKafkaTopics.AUDIO_MESSAGE_UPLOAD_REQUEST_KAFKA_TOPIC, request);
    }
}
