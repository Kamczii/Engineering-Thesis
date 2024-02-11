package org.once_a_day.file_storage.file_storage_app.service;

import org.once_a_day.database.model.ChatMessage;
import org.springframework.web.multipart.MultipartFile;

public interface AudioService {

    void process(Long userId, Long messageId, String content, String string);
}
