package org.once_a_day.sso.dto;

public record KeycloakUserDTO(String ssoId, String username, String firstName, String lastName, String email) {
}
