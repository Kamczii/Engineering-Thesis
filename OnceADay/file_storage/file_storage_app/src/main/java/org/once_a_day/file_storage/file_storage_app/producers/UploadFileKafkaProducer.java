package org.once_a_day.file_storage.file_storage_app.producers;

import lombok.RequiredArgsConstructor;
import org.once_a_day.core.kafka.CoreKafkaTopics;
import org.once_a_day.core.kafka.request.ActivityPictureChangedDTO;
import org.once_a_day.messenger.messenger_api.kafka.MessengerKafkaTopics;
import org.once_a_day.messenger.messenger_api.kafka.response.AudioMessageUploadedDTO;
import org.once_a_day.sso.dto.kafka.ProfileKafkaTopics;
import org.once_a_day.sso.dto.kafka.response.ProfilePictureChangedDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadFileKafkaProducer {
    private final KafkaTemplate<String, Object> uploadFileKafkaTemplate;

    public void send(ProfilePictureChangedDTO request) {
        uploadFileKafkaTemplate.send(ProfileKafkaTopics.PROFILE_PICTURE_UPLOADED_KAFKA_TOPIC, request);
    }

    public void send(ActivityPictureChangedDTO request) {
        uploadFileKafkaTemplate.send(CoreKafkaTopics.ACTIVITY_PICTURE_UPLOADED_KAFKA_TOPIC, request);
    }

    public void send(AudioMessageUploadedDTO request) {
        uploadFileKafkaTemplate.send(MessengerKafkaTopics.AUDIO_MESSAGE_UPLOADED_KAFKA_TOPIC, request);
    }
}
