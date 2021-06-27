package by.example.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}