package org.layer.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.layer.common.annotation.AtLeastNotNull;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class AtLeastNotNullValidator implements ConstraintValidator<AtLeastNotNull, Object> {
    private int min;

    @Override
    public void initialize(AtLeastNotNull constraintAnnotation) {
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Field[] fields = value.getClass().getDeclaredFields();
        int nonNullCount = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (Objects.nonNull(field.get(value))) {
                    nonNullCount++;
                    if (nonNullCount >= min) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
