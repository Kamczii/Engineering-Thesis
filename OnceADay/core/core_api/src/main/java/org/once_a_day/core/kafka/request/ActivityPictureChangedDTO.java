package org.once_a_day.core.kafka.request;

import lombok.*;
import org.once_a_day.dto.AbstractDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ActivityPictureChangedDTO extends AbstractDTO {
    Long userId;
    Long fileId;
}
