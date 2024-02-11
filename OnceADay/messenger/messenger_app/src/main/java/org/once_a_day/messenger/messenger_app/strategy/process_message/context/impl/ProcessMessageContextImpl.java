package org.once_a_day.messenger.messenger_app.strategy.process_message.context.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_app.strategy.process_message.context.ProcessMessageContext;
import org.once_a_day.messenger.messenger_app.strategy.process_message.strategy.ProcessMessageStrategy;
import org.once_a_day.messenger.messenger_common.enums.MessageType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;

@Component
@RequiredArgsConstructor
public class ProcessMessageContextImpl implements ProcessMessageContext {
    private final ProcessMessageStrategy processAudioMessageImpl;
    private final ProcessMessageStrategy processChatMessageImpl;

    EnumMap<MessageType, ProcessMessageStrategy> map = new EnumMap<>(MessageType.class);

    @PostConstruct
    void init() {
        map.put(MessageType.AUDIO, processAudioMessageImpl);
        map.put(MessageType.CHAT, processChatMessageImpl);
    }

    @Override
    public ProcessMessageStrategy resolve(MessageType type) {
        if (map.containsKey(type)) {
            return map.get(type);
        } else {
            throw new ApplicationException(ExceptionCode.CUSTOM_MESSAGE, "Cant handle this type of message");
        }
    }

}
