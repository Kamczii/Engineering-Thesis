package org.once_a_day.messenger.messenger_app.strategy.process_message.context;

import org.once_a_day.messenger.messenger_app.strategy.process_message.strategy.ProcessMessageStrategy;
import org.once_a_day.messenger.messenger_common.enums.MessageType;

public interface ProcessMessageContext {
    ProcessMessageStrategy resolve(MessageType type);
}
