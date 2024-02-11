package org.once_a_day.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private Class clazz;
    private String field;
    private String value;

    public ResourceNotFoundException(Class clazz, Long value) {
        super();
        this.clazz = clazz;
        this.field = "id";
        this.value = value.toString();
    }

    public ResourceNotFoundException(Class clazz, String field, String value) {
        super();
        this.clazz = clazz;
        this.field = field;
        this.value = value;
    }

    public ResourceNotFoundException(Class clazz, String field, Long value) {
        super();
        this.clazz = clazz;
        this.field = field;
        this.value = value.toString();
    }
}
