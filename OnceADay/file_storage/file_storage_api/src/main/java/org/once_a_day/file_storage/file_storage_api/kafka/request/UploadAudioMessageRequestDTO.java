package org.once_a_day.file_storage.file_storage_api.kafka.request;

import lombok.*;
import org.once_a_day.dto.AbstractDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UploadAudioMessageRequestDTO extends AbstractDTO {
    String content;
    Long userId;
    Long messageId;
}
