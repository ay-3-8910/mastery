package com.mastery.java.task.dto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * @author Sergey Tsynin
 */
public class EmployeeAgeConstraintValidator implements ConstraintValidator<EmployeeAgeConstraint, LocalDate> {

    @Override
    public boolean isValid(LocalDate employeeBirthday, ConstraintValidatorContext constraintValidatorContext) {
        return employeeBirthday.isBefore(LocalDate.now().minusYears(18));
    }
}
