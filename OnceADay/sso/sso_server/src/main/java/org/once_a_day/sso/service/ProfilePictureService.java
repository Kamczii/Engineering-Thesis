package org.once_a_day.sso.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {

    void changeProfilePicture(Long userId, Long fileId);
}
