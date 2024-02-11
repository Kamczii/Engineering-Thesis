package org.once_a_day.sso.service;

import org.once_a_day.sso.dto.CreateUserRequestDTO;

import java.util.UUID;

public interface UserService {
    void create(final UUID ssoId);

    Long create(final CreateUserRequestDTO user);
}
