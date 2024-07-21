package uk.co.scottishpower.smartreads.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uk.co.scottishpower.smartreads.validation.NotLowerThanPreviousReadingValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotLowerThanPreviousReadingValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotLowerThanPreviousRead {

    String message() default "Reading must be higher than the previous reading";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
