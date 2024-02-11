package org.once_a_day.file_storage.file_storage_app.service;

public interface FileService {

    Long uploadProfilePicture(Long userId, String fileName, byte[] bytes);

    Long uploadActivityImage(Long userId, String fileName, byte[] bytes);

    Long uploadAudio(Long userId, Long messageId, String fileName, byte[] bytes);

    byte[] downloadImage(Long fileId);
}
