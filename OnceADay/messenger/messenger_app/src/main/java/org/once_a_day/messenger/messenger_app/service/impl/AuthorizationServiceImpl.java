package org.once_a_day.messenger.messenger_app.service.impl;

import lombok.RequiredArgsConstructor;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.messenger.messenger_app.facade.impl.AuthenticationFacade;
import org.once_a_day.messenger.messenger_app.repository.UserRepository;
import org.once_a_day.messenger.messenger_app.service.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    @Override
    public Long getCurrentUserId() {
        final var sub = authenticationFacade.getSub();
        final var user = fetchUserSsoId(sub);
        return user.getId();
    }

    private User fetchUserSsoId(UUID ssoId) {
        return userRepository.findBySsoId(ssoId)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND, User.class.getSimpleName()));
    }
}
