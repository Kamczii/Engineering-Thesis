package org.once_a_day.listeners;

import lombok.RequiredArgsConstructor;
import org.once_a_day.core.kafka.CoreKafkaTopics;
import org.once_a_day.core.kafka.request.ActivityPictureChangedDTO;
import org.once_a_day.service.LabelService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FileUploadListener {
    private final LabelService labelService;

    @KafkaListener(topics = CoreKafkaTopics.ACTIVITY_PICTURE_UPLOADED_KAFKA_TOPIC, groupId = "sso-group")
    public void onUploadImageRequest(ActivityPictureChangedDTO message) {
        labelService.extractLabelsFromImage(message.getUserId(), message.getFileId());
    }
}
