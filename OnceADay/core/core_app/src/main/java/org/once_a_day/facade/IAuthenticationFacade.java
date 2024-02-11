package org.once_a_day.facade;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    UUID getSub();
}
