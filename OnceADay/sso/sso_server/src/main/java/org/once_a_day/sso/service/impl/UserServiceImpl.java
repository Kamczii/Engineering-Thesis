package org.once_a_day.sso.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.once_a_day.database.model.User;
import org.once_a_day.enumeration.ExceptionCode;
import org.once_a_day.exception.ApplicationException;
import org.once_a_day.sso.repository.UserRepository;
import org.once_a_day.sso.service.KeycloakClient;
import org.once_a_day.sso.service.UserService;
import org.once_a_day.sso.dto.CreateUserRequestDTO;
import org.once_a_day.sso.dto.KeycloakTokenRequestDTO;
import org.once_a_day.sso.dto.KeycloakUserDTO;
import org.once_a_day.sso.dto.ResetPasswordRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String DEFAULT_PASSWORD = "pass";
    private final UserRepository userRepository;
    private final KeycloakClient keycloakClient;
    @Value("${keycloak.admin.clientId}")
    private String clientId;

    @Value("${keycloak.admin.clientSecret}")
    private String clientSecret;

    @Value("${keycloak.admin.grantType}")
    private String grantType;

    @Override
    @Async
    public void create(final UUID ssoId) {
        try {
            Thread.sleep(2000);
            createLocal(ssoId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long create(final CreateUserRequestDTO user) {
        final var bearerToken = getKeycloakBearerToken();
        createKeycloakRepresentation(user, bearerToken);
        final var ssoId = getUserSsoId(bearerToken, user.getUsername());
        final var userId = createLocal(ssoId);
        setPassword(bearerToken, ssoId);
        return userId;
    }

    private Long createLocal(final UUID ssoId) {
        final var bearerToken = getKeycloakBearerToken();
        final var idpUser = keycloakClient.getUser(ssoId, bearerToken);
        final var entity = User.builder()
                .ssoId(ssoId)
                .username(idpUser.username())
                .firstName(idpUser.firstName())
                .lastName(idpUser.lastName())
                .build();
        final var save = userRepository.save(entity);
        return save.getId();
    }

    private void setPassword(final String bearerToken, final UUID ssoId) {
        final ResetPasswordRequestDTO pass = new ResetPasswordRequestDTO(DEFAULT_PASSWORD);
        keycloakClient.resetPassword(bearerToken, ssoId, pass);
    }

    private void createKeycloakRepresentation(final CreateUserRequestDTO user, final String bearerToken) {
        keycloakClient.createUser(bearerToken, user);
    }

    private UUID getUserSsoId(String bearer, String username) {
        return keycloakClient.getUsers( bearer, true, username).stream()
                .findFirst()
                .map(KeycloakUserDTO::ssoId)
                .map(UUID::fromString)
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND, "User not found in IDP"));
    }

    @NotNull
    private String getKeycloakBearerToken() {
        final var tokenRequest = KeycloakTokenRequestDTO.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type(grantType)
                .build();
        final var tokenResponse = keycloakClient.getToken(tokenRequest);
        return "Bearer " + tokenResponse.getAccessToken();
    }
}
