package org.once_a_day.sso.dto;

import lombok.Builder;
import lombok.Value;
import org.once_a_day.dto.AbstractDTO;

@Value
@Builder
public class CreateProfileDTO extends AbstractDTO {
    String description;
    String name;
}
