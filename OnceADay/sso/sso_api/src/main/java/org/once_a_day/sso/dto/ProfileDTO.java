package org.once_a_day.sso.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.once_a_day.dto.AbstractDTO;

@Setter
@Getter
@NoArgsConstructor
public class ProfileDTO extends AbstractDTO {
    String name;
    String description;
    String profilePicture;
}
