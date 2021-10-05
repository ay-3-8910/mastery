package com.mastery.java.task.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sergey Tsynin
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmployeeAgeConstraintValidator.class)
public @interface EmployeeAgeConstraint {
    String message() default "employee is too young";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
