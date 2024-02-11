package org.once_a_day.file_storage.file_storage_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.once_a_day.annotations.ValidImage;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.file_storage.file_storage_app.service.AuthorizationService;
import org.once_a_day.file_storage.file_storage_app.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class ProfilePictureController {
    private final FileService fileService;
    private final AuthorizationService authorizationService;

    @PostMapping("picture")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadProfilePicture(@Valid @ValidImage @RequestParam("picture") MultipartFile picture) {
        try {
            final var currentUserId = authorizationService.getCurrentUserId();
            fileService.uploadProfilePicture(currentUserId, picture.getOriginalFilename(), picture.getBytes());
        } catch (IOException e) {
            throw new ApplicationException(ExceptionCode.FILE_EXCEPTION);
        }
    }
}
