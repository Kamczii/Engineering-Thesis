package org.once_a_day.messenger.messenger_app.interceptor;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.ChatMessage;
import org.once_a_day.messenger.messenger_app.repository.MessageRepository;
import org.once_a_day.messenger.messenger_app.service.MessageService;
import org.once_a_day.messenger.messenger_app.service.WebsocketSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WebsocketOutboundInterceptor implements ChannelInterceptor {
    Logger logger = LoggerFactory.getLogger(WebsocketOutboundInterceptor.class);

    private final WebsocketSessionService websocketSessionService;
    private final MessageRepository messageRepository;
    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);
        if (SimpMessageType.MESSAGE.equals(accessor.getMessageType())) {
            final var exp = websocketSessionService.getExp(accessor.getSessionId());
            if (exp == null) {
                logger.info("Session id " + accessor.getSessionId() + " not exist");
                return null;
            } else if (exp < now()) {
                logger.info("Session id " + accessor.getSessionId() + " expired");
                websocketSessionService.remove(accessor.getSessionId());
                return null;
            }
            logger.info("Session id " + accessor.getSessionId() + " exist exist and expires in " + (exp - now()) + "seconds");
        }
        return message;
    }

    @Override
    public void postSend(final Message<?> message, final MessageChannel channel, final boolean sent) {
        final var accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);

        markAsSent(message);
    }



    private void markAsSent(final Message<?> message) {
        final Optional<Long> receiverId = getHeaderFromMessage(message, "receiverId");
        final Optional<Long> messageId = getHeaderFromMessage(message, "messageId");
        if (receiverId.isPresent() && messageId.isPresent()) {
            messageRepository.findById(messageId.get())
                    .filter(msg -> msg.getReceiver().getId().equals(receiverId.get()))
                    .map(this::markAsSent).ifPresent(messageRepository::save);
        }

    }

    private Optional<Long> getHeaderFromMessage(final Message<?> message, final String header) {
        return Optional.of(message.getHeaders())
                .map(headers -> headers.get("nativeHeaders"))
                .map(headers -> (Map<String, Object>) headers)
                .map(headers -> headers.get(header))
                .map(messageId -> (ArrayList<?>) messageId)
                .map(array -> Long.decode(array.get(0).toString()));
    }

    private ChatMessage markAsSent(ChatMessage message) {
        message.setSentTime(LocalDateTime.now());
        return message;
    }


    private static long now() {
        return new Date().getTime() / 1000;
    }
}
