package org.once_a_day.file_storage.file_storage_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.enums.FileType;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.exception.ResourceNotFoundException;
import org.once_a_day.file_storage.file_storage_app.repository.FileRepository;
import org.once_a_day.file_storage.file_storage_app.repository.UserRepository;
import org.once_a_day.file_storage.file_storage_app.service.FileService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Profile("local")
@Service
@RequiredArgsConstructor
public class LocalStorageFileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private static String PROFILE = "profile";
    private static String ACTIVITY = "activity";
    private static String AUDIO = "audio";
    private static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));

    @Override
    public Long uploadProfilePicture(Long userId, String fileName, byte[] bytes) {
        try {
            saveOnLocalStorage(PROFILE, fileName, bytes);
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
            final var resolveFileName = resolveFileName(ACTIVITY, fileName);
            saveOnLocalStorage(ACTIVITY, resolveFileName, bytes);
            final var fileDetails = buildActivityImage(resolveFileName, userId);
            final var save = fileRepository.save(fileDetails);
            return save.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long uploadAudio(Long userId, Long messageId, String fileName, byte[] bytes) {
        try {
            final var resolveFileName = resolveFileName(AUDIO, fileName);
            saveOnLocalStorage(AUDIO, resolveFileName, bytes);
            final var fileDetails = buildAudioFile(resolveFileName, userId);
            final var save = fileRepository.save(fileDetails);
            return save.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveFileName(final String folder, String fileName) {
        File dir = new File(TEMP_DIRECTORY, folder);
        if (dir.mkdir() || dir.isDirectory()) {
            var path = getPath(folder, fileName);
            final var split = fileName.split("\\.");
            int i = 1;
            while (Files.exists(path)) {
                fileName = split[0] + "(" + i++ + ")." + split[1];
                path = getPath(folder, fileName);
            }
        }
        return fileName;
    }

    private static Path getPath(final String folder, final String fileName) {
        return Paths.get(System.getProperty("java.io.tmpdir"), folder, fileName);
    }

    private void saveOnLocalStorage(final String folder, final String fileName, final byte[] bytes) throws IOException {
        File dir = new File(TEMP_DIRECTORY, folder);
        if (dir.mkdir() || dir.isDirectory()) {
            var path = getPath(folder, fileName);
            Files.write(path, bytes);
        }
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

    private FileDetails buildAudioFile(final String fileName, Long userId) {
        final var user = fetchUserById(userId);
        return FileDetails.builder()
                .type(FileType.AUDIO_MESSAGE)
                .bucket(AUDIO)
                .fileName(fileName)
                .user(user)
                .build();
    }

    @Override
    public byte[] downloadImage(Long fileId) {
        final var fileDetails = fetchImageById(fileId);
        final var bucket = fileDetails.getBucket();
        final var fileName = fileDetails.getFileName();
        final var path = getPath(bucket, fileName);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ResourceNotFoundException(File.class, fileId);
        }
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
