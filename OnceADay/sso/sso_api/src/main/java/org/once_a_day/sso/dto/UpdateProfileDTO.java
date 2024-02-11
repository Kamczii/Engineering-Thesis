package org.once_a_day.sso.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateProfileDTO {
    String description;
    String name;
}
