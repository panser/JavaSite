package ua.org.gostroy.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by panser on 6/20/2014.
 */
@Constraint(validatedBy = {UniqueForUserValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueForUser {
    String message() default "NOT UNIQUE";
//    String message() default "{UniqueKeyConstraint.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
