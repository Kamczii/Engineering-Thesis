package org.once_a_day.file_storage.file_storage_app.facade;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    UUID getSub();
}
