package org.layer.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.layer.common.validator.AtLeastNotNullValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastNotNullValidator.class)
public @interface AtLeastNotNull {
    String message() default "{min} 개 이상의 값이 필요해요.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int min() default 1;

}
