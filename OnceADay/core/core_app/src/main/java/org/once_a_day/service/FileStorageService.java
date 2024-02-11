package org.once_a_day.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "oad-file-storage")
public interface FileStorageService {
    @GetMapping(value = "files/{fileId}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] downloadImage(@PathVariable Long fileId);
}
