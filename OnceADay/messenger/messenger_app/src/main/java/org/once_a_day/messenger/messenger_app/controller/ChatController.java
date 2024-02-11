package org.once_a_day.messenger.messenger_app.controller;

import lombok.RequiredArgsConstructor;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;
import org.once_a_day.messenger.messenger_api.dto.ResponseChatMessageDTO;
import org.once_a_day.messenger.messenger_app.service.MessageService;
import org.once_a_day.messenger.messenger_common.enums.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@CrossOrigin

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;

    @MessageMapping("/matches/{matchId}/chat")
    public void sendMessageToUser(@Header("simpSessionId") String sessionId, @DestinationVariable Long matchId, @Payload InputChatMessageDTO message){
        if (!message.getType().equals(MessageType.CHAT)) {
            throw new ApplicationException(ExceptionCode.CUSTOM_MESSAGE, "Only text message allowed");
        }
        messageService.process(sessionId, matchId, message);
    }

    @PostMapping("/matches/{matchId}/chat")
    public ResponseEntity<Void> sendMessageToUser(@PathVariable Long matchId,
                                  @RequestBody InputChatMessageDTO message){
        messageService.process(matchId, message);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/matches/{matchId}/chat/messages")
    public ResponseEntity<Page<ResponseChatMessageDTO>> getChatMessages(@PathVariable Long matchId,
                                                                        @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC)
                                                                        Pageable pageable) {
        final var page = messageService.getMessages(matchId, pageable);
        return ResponseEntity.ok(page);
    }
}
