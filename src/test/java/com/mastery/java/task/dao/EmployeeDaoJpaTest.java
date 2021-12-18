package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.rest.excepton_handling.NotFoundMasteryException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Tsynin
 */
@SpringBootTest()
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EmployeeDaoJpaTest {

    @Autowired
    private EmployeeDaoJpa employeeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoTest.class);

    @Test
    public void shouldReturnEmployeesList() {
        LOGGER.debug("shouldReturnEmployeesList()");
        List<Employee> employees = employeeDao.getAllEmployees();
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployeeById() {
        LOGGER.debug("shouldReturnEmployeeById()");
        Integer id = 2;

        Employee employee = employeeDao.getEmployeeById(id);
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
        Exception exception = assertThrows(NotFoundMasteryException.class,
                () -> employeeDao.getEmployeeById(99));
        assertEquals("Employee id: 99 was not found in database", exception.getMessage());
    }

    @Test
    public void shouldCreateNewEmployee() {
        LOGGER.debug("shouldCreateNewEmployee()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now().minusYears(18));
        Employee savedEmployee = employeeDao.createEmployee(newEmployee);
        assertNotNull(savedEmployee);
        assertEquals(4, savedEmployee.getEmployeeId());
        assertEquals(employeesCountBefore + 1, employeeDao.getEmployeesCount());
        newEmployee.setEmployeeId(4);
        assertEquals(newEmployee, employeeDao.getEmployeeById(savedEmployee.getEmployeeId()));
    }

    @Test
    public void shouldReturnExceptionIfCreateTooYoungEmployee() {
        LOGGER.debug("shouldReturnExceptionIfCreateTooYoungEmployee()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now());
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> employeeDao.createEmployee(newEmployee));
        assertTrue(exception.getMessage().contains("The employee must be over 18 years old"));
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldReturnExceptionIfCreateEmployeeWithNullFirstName() {
        LOGGER.debug("shouldReturnExceptionIfCreateEmployeeWithNullFirstName()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setFirstName(null);
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> employeeDao.createEmployee(newEmployee));
        assertTrue(exception.getMessage().contains("Employee firstname cannot be empty"));
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldReturnExceptionIfCreateEmployeeWithNullLastName() {
        LOGGER.debug("shouldReturnExceptionIfCreateEmployeeWithNullLastName()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setLastName(null);
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> employeeDao.createEmployee(newEmployee));
        assertTrue(exception.getMessage().contains("Employee lastname cannot be empty"));
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldUpdateEmployee() {
        LOGGER.debug("shouldUpdateEmployee()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        String newJobTitle = "head of bottles washing";
        Integer employeeId = 2;

        Employee oldEmployee = employeeDao.getEmployeeById(employeeId);
        oldEmployee.setJobTitle(newJobTitle);
        employeeDao.updateEmployee(oldEmployee);
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
        assertEquals(newJobTitle, employeeDao.getEmployeeById(employeeId).getJobTitle());
    }

    @Test
    public void shouldReturnExceptionIfUpdateEmployeeWithUnknownId() {
        LOGGER.debug("shouldReturnExceptionIfUpdateEmployeeWithUnknownId()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee fakeEmployee = getFakeEmployee(128);
        Exception exception = assertThrows(NotFoundMasteryException.class,
                () -> employeeDao.updateEmployee(fakeEmployee));
        assertEquals("Employee was not found in database", exception.getMessage());
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    @Disabled
    public void shouldReturnExceptionIfUpdateEmployeeWithNullFirstName() {
        LOGGER.debug("shouldReturnExceptionIfUpdateEmployeeWithNullFirstName()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Integer employeeId = 2;

        Employee oldEmployee = getFakeEmployee(2);
        oldEmployee.setFirstName(null);
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> employeeDao.updateEmployee(oldEmployee));
        assertTrue(exception.getMessage().contains("Employee firstname cannot be empty"));
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    @Disabled
    public void shouldReturnExceptionIfUpdateEmployeeWithNullLastName() {
        LOGGER.debug("shouldReturnExceptionIfUpdateEmployeeWithNullLastName()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Integer employeeId = 2;

        Employee oldEmployee = employeeDao.getEmployeeById(employeeId);
        oldEmployee.setLastName(null);
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> employeeDao.updateEmployee(oldEmployee));
        assertTrue(exception.getMessage().contains("Employee lastname cannot be empty"));
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    @Disabled
    public void shouldReturnExceptionIfUpdateEmployeeWithLowAge() {
        LOGGER.debug("shouldReturnExceptionIfUpdateEmployeeWithLowAge()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Integer employeeId = 2;

        Employee oldEmployee = employeeDao.getEmployeeById(employeeId);
        oldEmployee.setDateOfBirth(LocalDate.now());
        Exception exception = assertThrows(ConstraintViolationException.class,
                () -> employeeDao.updateEmployee(oldEmployee));
        assertTrue(exception.getMessage().contains("The employee must be over 18 years old"));
        assertEquals(employeesCountBefore, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        employeeDao.deleteEmployee(1);
        assertEquals(employeesCountBefore - 1, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldReturnExceptionIfDeleteEmployeeWithUnknownId() {
        LOGGER.debug("shouldReturnExceptionIfDeleteEmployeeWithUnknownId()");
        Exception exception = assertThrows(NotFoundMasteryException.class,
                () -> employeeDao.deleteEmployee(128));
        assertEquals("Employee was not found in database", exception.getMessage());
    }

    @Test
    public void shouldReturnCountOfEmployees() {
        LOGGER.debug("shouldReturnCountOfEmployees()");
        Integer actualCount = employeeDao.getAllEmployees().size();
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
        employee.setDateOfBirth(LocalDate.now().minusYears(18));
        return employee;
    }

}