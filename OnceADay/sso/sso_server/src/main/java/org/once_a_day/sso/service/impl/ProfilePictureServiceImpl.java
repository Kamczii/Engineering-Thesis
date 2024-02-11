package org.once_a_day.sso.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.file_storage.file_storage_api.kafka.request.UploadProfilePictureRequestDTO;
import org.once_a_day.sso.repository.FileRepository;
import org.once_a_day.sso.repository.UserRepository;
import org.once_a_day.sso.service.AuthorizationService;
import org.once_a_day.sso.service.ProfilePictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfilePictureServiceImpl implements ProfilePictureService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Override
    public void changeProfilePicture(Long userId, Long fileId) {
        final var profile = fetchUserById(userId);
        final var file = fetchFileById(fileId);
        changeProfilePicture(profile, file);
        save(profile);
    }

    private UploadProfilePictureRequestDTO buildRequest(MultipartFile file) {
        try {
            final var base64 = Base64.encodeBase64String(file.getBytes());
            final var currentUserId = authorizationService.getCurrentUserId();
            return UploadProfilePictureRequestDTO.builder()
                    .fileName(file.getOriginalFilename())
                    .userId(currentUserId)
                    .content(base64)
                    .build();
        } catch (IOException e) {
            throw new ApplicationException(ExceptionCode.FILE_EXCEPTION);
        }
    }

    private static void changeProfilePicture(final User profile, final FileDetails file) {
        profile.setAvatar(file);
    }

    private FileDetails fetchFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND, FileDetails.class.getSimpleName()));
    }

    private User fetchUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND, User.class.getSimpleName()));
    }

    private void save(final User user) {
        userRepository.save(user);
    }
}
