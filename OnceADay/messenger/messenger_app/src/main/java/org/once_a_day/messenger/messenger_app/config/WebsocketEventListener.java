package org.once_a_day.messenger.messenger_app.config;

import lombok.RequiredArgsConstructor;
import org.once_a_day.messenger.messenger_app.service.WebsocketSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebsocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    private final WebsocketSessionService websocketSessionService;

    @EventListener
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        websocketSessionService.remove(event.getSessionId());
    }
}
