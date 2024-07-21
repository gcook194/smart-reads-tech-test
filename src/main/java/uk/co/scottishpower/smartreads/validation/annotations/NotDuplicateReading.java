package uk.co.scottishpower.smartreads.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uk.co.scottishpower.smartreads.validation.NotDuplicateReadingValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NotDuplicateReadingValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotDuplicateReading {

    String message() default "A reading for this date already exists";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
