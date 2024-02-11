package org.once_a_day.file_storage.file_storage_app.facade.impl;


import org.once_a_day.file_storage.file_storage_app.facade.IAuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UUID getSub() {
        final var authentication = getAuthentication();
        return UUID.fromString(authentication.getName());
    }
}
