package com.mastery.java.task.dto;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Sergey Tsynin
 */
class EmployeeTest {

    @Test
    public void getters_setters_test() {
        Employee employee = new Employee();
        Integer newId = 32768;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        Integer newDepartmentId = 42;
        String newJobTitle = "newJobTitle";
        Gender newGender = Gender.MALE;
        LocalDate newDateOfBirth = LocalDate.of(2021, 1, 1);

        employee.setEmployeeId(newId);
        employee.setFirstName(newFirstName);
        employee.setLastName(newLastName);
        employee.setDepartmentId(newDepartmentId);
        employee.setJobTitle(newJobTitle);
        employee.setGender(newGender);
        employee.setDateOfBirth(newDateOfBirth);

        assertEquals(newId, employee.getEmployeeId(), "wrong employee id");
        assertEquals(newFirstName, employee.getFirstName());
        assertEquals(newLastName, employee.getLastName());
        assertEquals(newDepartmentId, employee.getDepartmentId());
        assertEquals(newJobTitle, employee.getJobTitle());
        assertEquals(newGender, employee.getGender());
        assertEquals(newDateOfBirth, employee.getDateOfBirth());
    }

    @Test
    public void shouldReturnErrorWithYoungEmployee() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Employee employee = new Employee();
        LocalDate newDateOfBirth = LocalDate.now().minusYears(18);
        employee.setDateOfBirth(newDateOfBirth);

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        assertFalse(violations.isEmpty());
    }
}
