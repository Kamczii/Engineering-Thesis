package org.once_a_day.messenger.messenger_app.advice;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.once_a_day.database.model.Match;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO;
import org.once_a_day.messenger.messenger_app.repository.MatchRepository;
import org.once_a_day.messenger.messenger_app.service.AuthorizationService;
import org.once_a_day.messenger.messenger_app.service.WebsocketSessionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MessageAdvice {
    private final MatchRepository matchRepository;
    private final AuthorizationService authorizationService;
    private final WebsocketSessionService websocketSessionService;

    @Before(value = "execution(* org.once_a_day.messenger.messenger_app.service.MessageService.process(..)) " +
            "&& args(sessionId,matchId,message)", argNames = "sessionId,matchId,message")
    public void checkIfSessionExists(String sessionId, Long matchId, InputChatMessageDTO message) {
        final boolean sessionExist = websocketSessionService.sessionExist(sessionId);
        if (!sessionExist) {
            throw new ApplicationException(ExceptionCode.WS_SESSION_EXPIRED);
        }
    }

    @Before(value = "execution(* org.once_a_day.messenger.messenger_app.service.MessageService.process(.., Long, org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO)) " +
            "&& args(..,matchId,message)", argNames = "matchId,message")
    public void checkIfMatchExists(Long matchId, InputChatMessageDTO message) {
        authorizationService.getCurrentUserId();
        final var receiver = message.getReceiverId();
        final var sender = message.getSenderId();
        Optionals.firstNonEmpty(
                () -> matchRepository.findByIdAndUserOneIdAndUserTwoId(matchId, sender, receiver),
                () -> matchRepository.findByIdAndUserOneIdAndUserTwoId(matchId, receiver, sender)
        ).orElseThrow(() -> new ApplicationException(ExceptionCode.USERS_NOT_MATCHED));
    }

    @Before(value = "execution(* org.once_a_day.messenger.messenger_app.service.MessageService.process(.., org.once_a_day.messenger.messenger_api.dto.InputChatMessageDTO)) " +
            "&& args(..,message)", argNames = "message")
    public void checkIfSender(InputChatMessageDTO message) {
        //TODO: senderId equals currentUserId
    }

    @Before(value = "execution(* org.once_a_day.messenger.messenger_app.service.MessageService.getMessages(..)) " +
            "&& args(matchId,pageable)", argNames = "matchId,pageable")
    public void beforeGetMessages(Long matchId, Pageable pageable) {
        final var currentUserId = authorizationService.getCurrentUserId();
        if (!userBelongsToMatch(matchId, currentUserId)) {
            throw new ApplicationException(ExceptionCode.USERS_NOT_MATCHED);
        }
    }

    private boolean userBelongsToMatch(Long matchId, Long userId) {
        final var match = fetchMatch(matchId);
        return match.getUserOne().getId().equals(userId) || match.getUserTwo().getId().equals(userId);
    }

    private Match fetchMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND));
    }
}
