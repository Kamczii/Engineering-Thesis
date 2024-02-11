package org.once_a_day.sso.listeners;

import lombok.RequiredArgsConstructor;
import org.once_a_day.sso.service.ProfilePictureService;
import org.once_a_day.sso.dto.kafka.ProfileKafkaTopics;
import org.once_a_day.sso.dto.kafka.response.ProfilePictureChangedDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FileUploadListener {
    private final ProfilePictureService profilePictureService;

    @KafkaListener(topics = ProfileKafkaTopics.PROFILE_PICTURE_UPLOADED_KAFKA_TOPIC, groupId = "sso-group")
    public void onUploadImageRequest(ProfilePictureChangedDTO message) {
        profilePictureService.changeProfilePicture(message.getUserId(), message.getFileId());
    }
}
