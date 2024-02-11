package org.once_a_day.file_storage.file_storage_app.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(name = "http://x.x.x.x:8092")
public interface AudioCutService {
    @PostMapping(value = "/upload", produces = "audio/mpeg", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    byte[] cut(@RequestBody Map<String, ?> requestBody);
}
