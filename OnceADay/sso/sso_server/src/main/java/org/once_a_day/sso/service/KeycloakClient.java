package org.once_a_day.sso.service;

import org.once_a_day.sso.config.KeycloakFeignClientConfig;
import org.once_a_day.sso.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "keycloak", url = "http://x.x.x.x:8080", configuration = KeycloakFeignClientConfig.class)
public interface KeycloakClient {
    @PostMapping(value = "/realms/master/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KeycloakTokenResponseDTO getToken(@RequestBody KeycloakTokenRequestDTO data);

    @PostMapping(value = "/admin/realms/once_a_day/users")
    void createUser(@RequestHeader("Authorization") String token, @RequestBody CreateUserRequestDTO create);

    @GetMapping(value = "/admin/realms/once_a_day/users")
    List<KeycloakUserDTO> getUsers(@RequestHeader("Authorization") String token,
                                   @RequestParam("exact") boolean exact,
                                   @RequestParam("username") String username);

    @GetMapping(value = "/admin/realms/once_a_day/users/{ssoId}")
    KeycloakUserDTO getUser(@PathVariable UUID ssoId,
                            @RequestHeader("Authorization") String token);

    @PutMapping(value = "/admin/realms/once_a_day/users/{userId}/reset-password")
    void resetPassword(@RequestHeader("Authorization") String token, @PathVariable UUID userId, @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO);
}
