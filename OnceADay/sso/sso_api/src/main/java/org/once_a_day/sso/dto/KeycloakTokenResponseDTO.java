package org.once_a_day.sso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class KeycloakTokenResponseDTO {
    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("expires_in")
    String expiresIn;

    @JsonProperty("refresh_expires_in")
    String refreshExpiresIn;

    @JsonProperty("token_type")
    String tokenType;

    @JsonProperty("not-before-polic")
    String notBeforePolicy;

    @JsonProperty("scope")
    String scope;
}
