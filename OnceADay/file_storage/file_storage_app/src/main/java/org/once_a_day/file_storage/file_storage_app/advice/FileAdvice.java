package org.once_a_day.file_storage.file_storage_app.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.once_a_day.core.kafka.request.ActivityPictureChangedDTO;
import org.once_a_day.file_storage.file_storage_app.producers.UploadFileKafkaProducer;
import org.once_a_day.messenger.messenger_api.kafka.response.AudioMessageUploadedDTO;
import org.once_a_day.sso.dto.kafka.response.ProfilePictureChangedDTO;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FileAdvice {
    private final UploadFileKafkaProducer uploadFileKafkaProducer;

    @AfterReturning(pointcut = "execution(* org.once_a_day.file_storage.file_storage_app.service.FileService.uploadProfilePicture(..)) && args(userId, fileName, bytes))", returning="fileId")
    public void afterUploadProfileImage(Long userId, String fileName, byte[] bytes, Long fileId) {
        final var message = buildProfilePictureChangedMessage(userId, fileId);
        uploadFileKafkaProducer.send(message);
    }

    @AfterReturning(pointcut = "execution(* org.once_a_day.file_storage.file_storage_app.service.FileService.uploadActivityImage(..)) && args(userId, fileName, bytes))", returning="fileId")
    public void afterUploadActivityImage(Long userId, String fileName, byte[] bytes, Long fileId) {
        final var message = buildActivityUploadedMessage(userId, fileId);
        uploadFileKafkaProducer.send(message);
    }

    @AfterReturning(pointcut = "execution(* org.once_a_day.file_storage.file_storage_app.service.FileService.uploadAudio(..)) && args(userId, messageId, fileName, bytes))", returning="fileId")
    public void afterUploadAudio(Long userId, Long messageId, String fileName, byte[] bytes, Long fileId) {
        final var message = buildAudioUploadedMessage(userId, fileId, messageId);
        uploadFileKafkaProducer.send(message);
    }

    private AudioMessageUploadedDTO buildAudioUploadedMessage(final Long userId, final Long fileId, final Long messageId) {
        return AudioMessageUploadedDTO.builder()
                .userId(userId)
                .fileId(fileId)
                .messageId(messageId)
                .build();
    }


    private static ProfilePictureChangedDTO buildProfilePictureChangedMessage(Long userId, Long fileId) {
        return ProfilePictureChangedDTO.builder()
                .userId(userId)
                .fileId(fileId)
                .build();
    }

    private static ActivityPictureChangedDTO buildActivityUploadedMessage(Long userId, Long fileId) {
        return ActivityPictureChangedDTO.builder()
                .userId(userId)
                .fileId(fileId)
                .build();
    }
}
