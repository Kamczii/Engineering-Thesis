package org.once_a_day.file_storage.file_storage_app.controller;

import lombok.RequiredArgsConstructor;
import org.once_a_day.file_storage.file_storage_app.service.FileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping(value = "{fileId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] downloadImage(@PathVariable Long fileId) {
        return fileService.downloadImage(fileId);
    }
}
