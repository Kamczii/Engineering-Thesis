package org.once_a_day.messenger.messenger_api.kafka.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AudioMessageUploadedDTO {
    Long userId;
    Long fileId;
    Long messageId;
}
