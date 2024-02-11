package org.once_a_day.messenger.messenger_app.facade;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    UUID getSub();
}
