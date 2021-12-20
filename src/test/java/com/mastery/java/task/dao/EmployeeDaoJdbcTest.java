package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.rest.excepton_handling.NotFoundMasteryException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Tsynin
 */
@DataJdbcTest
@Import(EmployeeDaoJdbc.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@PropertySource({"classpath:sql.properties"})
class EmployeeDaoJdbcTest {

    @Autowired
    EmployeeDaoJdbc employeeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoJdbcTest.class);

    @Test
    public void shouldReturnEmployeesList() {
        LOGGER.debug("shouldReturnEmployeesList()");

        // when
        List<Employee> employees = employeeDao.getAllEmployees();

        // then
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployee() {
        LOGGER.debug("shouldReturnEmployee()");

        // when
        Employee employee = employeeDao.getEmployeeById(2);

        // then
        assertEquals(2, employee.getEmployeeId());
        assertEquals("Rudolph", employee.getFirstName());
        assertEquals("the Deer", employee.getLastName());
        assertEquals(2, employee.getDepartmentId());
        assertEquals("bottles washer", employee.getJobTitle());
        assertEquals(Gender.UNSPECIFIED, employee.getGender());
        assertEquals(LocalDate.of(2000, 8, 16), employee.getDateOfBirth());
    }

    @Test
    public void shouldReturnExceptionWithUnknownEmployeeId() {
        LOGGER.debug("shouldReturnExceptionWithUnknownEmployeeId()");

        // then
        Exception exception = assertThrows(NotFoundMasteryException.class,
                () -> employeeDao.getEmployeeById(99));
        assertEquals("Employee id: 99 was not found in database", exception.getMessage());
    }

    @Test
    public void shouldCreateNewEmployee() {
        LOGGER.debug("shouldCreateNewEmployee()");

        // given
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now().minusYears(18));

        // when
        Employee savedEmployee = employeeDao.createEmployee(newEmployee);

        // then
        assertNotNull(savedEmployee);
        assertEquals(employeesCountBefore + 1, employeeDao.getEmployeesCount());
        newEmployee.setEmployeeId(savedEmployee.getEmployeeId());
        assertEquals(newEmployee, employeeDao.getEmployeeById(savedEmployee.getEmployeeId()));
    }

    @Test
    public void shouldUpdateEmployee() {
        LOGGER.debug("shouldUpdateEmployee()");

        // given
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Integer employeeId = 2;
        Employee oldEmployee = getFakeEmployee(employeeId);

        // when
        employeeDao.updateEmployee(oldEmployee);

        // then
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
        assertEquals(oldEmployee, employeeDao.getEmployeeById(employeeId));
    }

    @Test
    public void shouldReturnExceptionIfUpdateEmployeeWithUnknownId() {
        LOGGER.debug("shouldReturnExceptionIfUpdateEmployeeWithUnknownId()");

        // given
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee fakeEmployee = getFakeEmployee(128);

        // then
        Exception exception = assertThrows(NotFoundMasteryException.class,
                () -> employeeDao.updateEmployee(fakeEmployee));
        assertEquals("Employee id: 128 was not found in database", exception.getMessage());
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");

        // given
        Integer employeesCountBefore = employeeDao.getEmployeesCount();

        // when
        employeeDao.deleteEmployee(1);

        // then
        assertEquals(employeesCountBefore - 1, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldReturnExceptionIfDeleteEmployeeWithUnknownId() {
        LOGGER.debug("shouldReturnExceptionIfDeleteEmployeeWithUnknownId()");

        // then
        Exception exception = assertThrows(NotFoundMasteryException.class,
                () -> employeeDao.deleteEmployee(128));
        assertEquals("Employee id: 128 was not found in database", exception.getMessage());
    }

    @Test
    public void shouldReturnCountOfEmployees() {
        LOGGER.debug("shouldReturnCountOfEmployees()");

        // when
        Integer actualCount = employeeDao.getAllEmployees().size();

        // then
        assertEquals(actualCount, employeeDao.getEmployeesCount());
    }

    private Employee getFakeEmployee(@SuppressWarnings("SameParameterValue") Integer id) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName("FirstName" + id);
        employee.setLastName("LastName" + id);
        employee.setDepartmentId(id);
        employee.setJobTitle("JobTitle" + id);
        employee.setGender(Gender.UNSPECIFIED);
        employee.setDateOfBirth(LocalDate.now());
        return employee;
    }
}
