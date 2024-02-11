package org.once_a_day.sso.dto;

import lombok.Value;

@Value
public class ResetPasswordRequestDTO {
    String type = "password";
    String value;
    boolean temporary = false;
}
