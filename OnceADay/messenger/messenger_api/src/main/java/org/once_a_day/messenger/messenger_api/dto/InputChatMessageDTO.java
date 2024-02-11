package org.once_a_day.messenger.messenger_api.dto;

import lombok.Builder;
import lombok.Value;
import org.once_a_day.messenger.messenger_common.enums.MessageType;

@Value
@Builder
public class InputChatMessageDTO {
    String content;
    Long senderId;
    MessageType type;
    Long receiverId;
}
