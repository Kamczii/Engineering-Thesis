package org.once_a_day.sso.dto.kafka.response;

import lombok.*;
import org.once_a_day.dto.AbstractDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProfilePictureChangedDTO extends AbstractDTO {
    Long userId;
    Long fileId;
}
