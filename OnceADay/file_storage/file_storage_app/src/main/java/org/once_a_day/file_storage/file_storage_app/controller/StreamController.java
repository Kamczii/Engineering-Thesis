package org.once_a_day.file_storage.file_storage_app.controller;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.FileDetails;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.file_storage.file_storage_app.repository.FileRepository;
import org.once_a_day.file_storage.file_storage_app.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("streams")
@RequiredArgsConstructor
public class StreamController {
    private static final String FORMAT = "classpath:%s/%s";

    private final ResourceLoader resourceLoader;
    private final FileRepository fileRepository;
    private final FileService fileService;

    @GetMapping(value = "audio/{fileId}", produces = "audio/mpeg")
    public Mono<Resource> getAudio(@PathVariable Long fileId, @RequestHeader("Range") String range) {
        final var fileDetails = fetchFileById(fileId);
        System.out.println("Loading range " + range);
        final var path = String.join("/", System.getProperty("java.io.tmpdir"), fileDetails.getBucket(), fileDetails.getFileName());
        return Mono.fromSupplier(() -> {
            try {
                return new InputStreamResource(new FileInputStream(path));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private FileDetails fetchFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND, FileDetails.class.getSimpleName()));
    }
}
