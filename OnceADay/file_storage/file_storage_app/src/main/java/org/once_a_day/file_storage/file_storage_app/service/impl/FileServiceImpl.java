package org.once_a_day.file_storage.file_storage_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NotImplementedException;
import org.once_a_day.database.enums.FileType;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.file_storage.file_storage_app.repository.FileRepository;
import org.once_a_day.file_storage.file_storage_app.repository.UserRepository;
import org.once_a_day.file_storage.file_storage_app.service.FileService;
import org.once_a_day.file_storage.file_storage_app.service.S3BucketService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Profile("!local")
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final S3BucketService s3BucketService;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private static String PROFILE = "profile";
    private static String ACTIVITY = "activity";

    @Override
    public Long uploadProfilePicture(Long userId, String fileName, byte[] bytes) {
        try {
            s3BucketService.uploadFile(PROFILE, fileName, bytes);
            final var fileDetails = buildProfileImage(fileName, userId);
            final var save = fileRepository.save(fileDetails);
            return save.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long uploadActivityImage(Long userId, String fileName, byte[] bytes) {
        try {
            s3BucketService.uploadFile(ACTIVITY, fileName, bytes);
            final var fileDetails = buildActivityImage(fileName, userId);
            final var save = fileRepository.save(fileDetails);
            return save.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long uploadAudio(final Long userId, final Long messageId, final String fileName, final byte[] bytes) {
        throw new NotImplementedException();
    }

    private FileDetails buildProfileImage(final String fileName, Long userId) {
        final var user = fetchUserById(userId);
        return FileDetails.builder()
                        .type(FileType.PROFILE_IMAGE)
                        .bucket(PROFILE)
                        .fileName(fileName)
                        .user(user)
                        .build();
    }

    private FileDetails buildActivityImage(final String fileName, Long userId) {
        final var user = fetchUserById(userId);
        return FileDetails.builder()
                .type(FileType.ACTIVITY_IMAGE)
                .bucket(ACTIVITY)
                .fileName(fileName)
                .user(user)
                .build();
    }

    @Override
    public byte[] downloadImage(Long fileId) {
        final var fileDetails = fetchImageById(fileId);
        final var bucket = fileDetails.getBucket();
        final var fileName = fileDetails.getFileName();
        return s3BucketService.downloadFile(bucket, fileName);
    }

    private FileDetails fetchImageById(final Long fileId) {
        return fileRepository.findByIdAndTypeIn(fileId, FileType.ACTIVITY_IMAGE, FileType.PROFILE_IMAGE)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }

    private User fetchUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }
}
