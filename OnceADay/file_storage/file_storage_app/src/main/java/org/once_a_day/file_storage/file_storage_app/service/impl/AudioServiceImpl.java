package org.once_a_day.file_storage.file_storage_app.service.impl;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.once_a_day.file_storage.file_storage_app.service.AudioService;
import org.once_a_day.file_storage.file_storage_app.service.FileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {
    private final FileService fileService;

    @Override
    public void process(final Long userId, final Long messageId, final String content, final String fileName) {
        byte[] decodedBytes = Base64.getDecoder().decode(content);
//        final byte[] cut = send(decodedBytes);
        fileService.uploadAudio(userId, messageId, fileName, decodedBytes);
    }

    private byte[] send(byte[] fileContent) {
        try {
            File file = new File( System.getProperty("java.io.tmpdir")+"/file.mp3");
            FileUtils.writeByteArrayToFile( file, fileContent );
            InputStream response = Unirest.post("http://x.x.x.x:8092/upload")
                    .field("file", file).asBinary().getRawBody();;
            return IOUtils.toByteArray(response);
        } catch (IOException | UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
