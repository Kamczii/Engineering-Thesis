package org.once_a_day.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.once_a_day.validators.ValidImageValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.PARAMETER, ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ValidImageValidator.class)
@Documented
public @interface ValidImage {

    String message() default "File is not a image";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}