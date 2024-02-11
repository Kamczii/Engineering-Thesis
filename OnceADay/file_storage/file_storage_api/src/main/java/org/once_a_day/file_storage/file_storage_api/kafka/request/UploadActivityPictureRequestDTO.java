package org.once_a_day.file_storage.file_storage_api.kafka.request;

import lombok.*;
import org.once_a_day.dto.AbstractDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UploadActivityPictureRequestDTO extends AbstractDTO {
    String fileName;
    String content;
    Long userId;
}
