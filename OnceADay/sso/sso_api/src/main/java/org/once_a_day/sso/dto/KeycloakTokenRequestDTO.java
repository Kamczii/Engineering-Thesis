package org.once_a_day.sso.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class KeycloakTokenRequestDTO {
    String client_id;

    String grant_type;

    String client_secret;
}
