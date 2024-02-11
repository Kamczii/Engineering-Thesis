package org.once_a_day.sso.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateUserRequestDTO {
    String firstName;
    String lastName;
    String email;
    String enabled;
    String username;
}
