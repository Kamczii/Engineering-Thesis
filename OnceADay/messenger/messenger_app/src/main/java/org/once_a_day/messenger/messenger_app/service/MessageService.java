package org.once_a_day.messenger.messenger_app.service;

import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;
import org.once_a_day.messenger.messenger_api.dto.ResponseChatMessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    void process(final String sessionId, final Long matchId, InputChatMessageDTO inputChatMessage);

    void process(Long matchId, InputChatMessageDTO message);

    Page<ResponseChatMessageDTO> getMessages(Long matchId, Pageable pageable);
}
