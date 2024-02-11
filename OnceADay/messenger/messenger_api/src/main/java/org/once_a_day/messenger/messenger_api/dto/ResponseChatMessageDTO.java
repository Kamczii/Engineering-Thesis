package org.once_a_day.messenger.messenger_api.dto;

import lombok.Builder;
import lombok.Value;
import org.once_a_day.messenger.messenger_common.enums.MessageType;

import java.time.LocalDateTime;

@Builder
@Value
public class ResponseChatMessageDTO {
    Long id;
    String content;
    Long senderId;
    MessageType type;
    Long receiverId;
    LocalDateTime createdTime;
    String attachmentUrl;
    LocalDateTime sentTime;
}
