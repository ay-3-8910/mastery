package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Tsynin
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EmployeeJpaRepositoryTest {

    @Autowired
    EmployeeJpaRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeJpaRepositoryTest.class);

    @Test
    public void shouldReturnEmployeesList() {
        LOGGER.debug("shouldReturnEmployeesList()");

        // when
        List<Employee> employees = repository.findAll();

        // then
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployeeById() {
        LOGGER.debug("shouldReturnEmployeeById()");

        // when
        Optional<Employee> optionalEmployee = repository.findById(2);

        // then
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();

        assertEquals(2, employee.getEmployeeId());
        assertEquals("Rudolph", employee.getFirstName());
        assertEquals("the Deer", employee.getLastName());
        assertEquals(2, employee.getDepartmentId());
        assertEquals("bottles washer", employee.getJobTitle());
        assertEquals(Gender.UNSPECIFIED, employee.getGender());
        assertEquals(LocalDate.of(2000, 8, 16), employee.getDateOfBirth());
    }

    @Test
    public void shouldReturnEmployeeByName() {
        LOGGER.debug("shouldReturnEmployeeByName()");

        // when
        List<Employee> employees = repository.findByFirstNameContainsAndLastNameContains("Rudolph", "");

        // then
        assertEquals(1, employees.size());
    }

    @Test
    public void shouldCreateNewEmployee() {
        LOGGER.debug("shouldCreateNewEmployee()");

        // given
        long employeesCountBefore = repository.count();
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now().minusYears(18));

        // when
        Employee returnedEmployee = repository.save(newEmployee);

        // then
        assertNotNull(returnedEmployee);
        assertEquals(employeesCountBefore + 1, repository.count());
        newEmployee.setEmployeeId(returnedEmployee.getEmployeeId());
        Optional<Employee> optionalSavedEmployee = repository.findById(returnedEmployee.getEmployeeId());
        assertTrue(optionalSavedEmployee.isPresent());
        assertEquals(newEmployee, optionalSavedEmployee.get());
    }

    @Test
    public void shouldUpdateEmployee() {
        LOGGER.debug("shouldUpdateEmployee()");

        // given
        long employeesCountBefore = repository.count();
        Integer employeeId = 2;
        Employee oldEmployee = getFakeEmployee(employeeId);

        // when
        repository.save(oldEmployee);

        // then
        assertEquals(employeesCountBefore, repository.count());
        Optional<Employee> optionalSavedEmployee = repository.findById(2);
        assertTrue(optionalSavedEmployee.isPresent());
        assertEquals(oldEmployee, optionalSavedEmployee.get());
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");

        // given
        long employeesCountBefore = repository.count();

        // when
        repository.deleteById(1);

        // then
        assertEquals(employeesCountBefore - 1, repository.count());
    }

    @Test
    public void shouldReturnCountOfEmployees() {
        LOGGER.debug("shouldReturnCountOfEmployees()");

        // when
        long actualCount = repository.findAll().size();

        // then
        assertEquals(actualCount, repository.count());
    }

    private Employee getFakeEmployee(@SuppressWarnings("SameParameterValue") Integer id) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName("FirstName" + id);
        employee.setLastName("LastName" + id);
        employee.setDepartmentId(id);
        employee.setJobTitle("JobTitle" + id);
        employee.setGender(Gender.UNSPECIFIED);
        employee.setDateOfBirth(LocalDate.now().minusYears(18));
        return employee;
    }
}
