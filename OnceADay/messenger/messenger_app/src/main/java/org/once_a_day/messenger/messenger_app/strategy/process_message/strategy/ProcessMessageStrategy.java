package org.once_a_day.messenger.messenger_app.strategy.process_message.strategy;

import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;

public interface ProcessMessageStrategy {
    void process(final Long matchId, InputChatMessageDTO message);
}
