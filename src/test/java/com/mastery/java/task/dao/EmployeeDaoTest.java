package com.mastery.java.task.dao;

import com.mastery.java.task.config.AppConfiguration;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Tsynin
 */
@DataJdbcTest
@Import(EmployeeDaoJdbc.class)
@ContextConfiguration(classes = AppConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@PropertySource({"classpath:sql.properties"})
class EmployeeDaoTest {

    @Autowired
    EmployeeDaoJdbc employeeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoTest.class);

    @Test
    public void shouldReturnEmployeesList() {
        LOGGER.debug("shouldReturnEmployeesList()");
        List<Employee> employees = employeeDao.getAllEmployees();
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployee() {
        LOGGER.debug("shouldReturnEmployee()");
        Employee employee = employeeDao.getEmployeeById(2);
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
        Exception exception = assertThrows(NotFoundMasteryException.class, () -> employeeDao.getEmployeeById(99));
        assertEquals("Employee id: 99 was not found in database", exception.getMessage());
    }

    @Test
    public void shouldReturnCountOfEmployees() {
        LOGGER.debug("shouldReturnCountOfEmployees()");
        Integer actualCount = employeeDao.getAllEmployees().size();
        assertEquals(actualCount, employeeDao.getEmployeesCount());
    }

    @Test
    public void shouldSaveNewEmployee() {
        LOGGER.debug("shouldSaveNewEmployee()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        Employee newEmployee = getFakeEmployee(128);
        Employee savedEmployee = employeeDao.createEmployee(newEmployee);
        assertEquals(4, savedEmployee.getEmployeeId());
        assertEquals(employeesCountBefore + 1, employeeDao.getEmployeesCount());
        assertEquals(newEmployee, employeeDao.getEmployeeById(savedEmployee.getEmployeeId()));
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
        Employee fakeEmployee = getFakeEmployee(128);
        Exception exception = assertThrows(NotFoundMasteryException.class, () -> employeeDao.updateEmployee(fakeEmployee));
        assertEquals("Employee was not found in database", exception.getMessage());
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");
        Integer employeesCountBefore = employeeDao.getEmployeesCount();
        employeeDao.deleteEmployee(1);
        assertEquals(employeesCountBefore - 1, employeeDao.getEmployeesCount());
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
