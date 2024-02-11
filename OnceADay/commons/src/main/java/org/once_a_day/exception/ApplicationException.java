package org.once_a_day.exception;


import lombok.Getter;
import org.once_a_day.enumeration.ExceptionCode;

@Getter
public class ApplicationException extends RuntimeException {
    private final ExceptionCode code;
    private final String message;

    public ApplicationException(ExceptionCode code) {
        this.code = code;
        this.message = null;
    }

    public ApplicationException(ExceptionCode code, String message) {
        this.code = code;
        this.message = message;
    }
}
