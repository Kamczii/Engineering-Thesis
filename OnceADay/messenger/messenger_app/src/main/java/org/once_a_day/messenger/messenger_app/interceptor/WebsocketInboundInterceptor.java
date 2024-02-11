package org.once_a_day.messenger.messenger_app.interceptor;

import lombok.RequiredArgsConstructor;
import org.once_a_day.messenger.messenger_app.service.WebsocketSessionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketInboundInterceptor implements ChannelInterceptor {
    private final String X_AUTH_HEADER = "X-AUTH-HEADER";
    private final WebsocketSessionService websocketSessionService;

    @Override
    public Message preSend(final Message message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            final String token = accessor.getFirstNativeHeader(X_AUTH_HEADER);
            websocketSessionService.put(accessor.getSessionId(), token);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            websocketSessionService.remove(accessor.getSessionId());
        }
        return message;
    }
}
