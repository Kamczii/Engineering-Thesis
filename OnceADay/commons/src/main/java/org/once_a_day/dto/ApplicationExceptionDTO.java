package org.once_a_day.dto;

import lombok.Builder;
import lombok.Value;
import org.once_a_day.enumeration.ExceptionCode;

@Builder
@Value
public class ApplicationExceptionDTO extends AbstractDTO {
    ExceptionCode code;
    String message;
}
