package com.mastery.java.task.dao;

import com.mastery.java.task.config.AppConfiguration;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Tsynin
 */
@DataJdbcTest
@Import(EmployeeDao.class)
@ContextConfiguration(classes = AppConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@PropertySource({"classpath:sql.properties"})
class EmployeeDaoTest {

    @Autowired
    EmployeeDao employeeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoTest.class);

    @Test
    public void shouldReturnEmployeesList() {
        LOGGER.debug("shouldReturnEmployeesList()");
        List<Employee> employees = employeeDao.findAll();
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployee() {
        LOGGER.debug("shouldReturnEmployee()");
        Optional<Employee> optionalEmployee = employeeDao.findById(2);
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();
        assertEquals(2, employee.getEmployeeId());
        assertEquals("Rudolph", employee.getFirstName());
        assertEquals("the Deer", employee.getLastName());
        assertEquals(2, employee.getDepartmentId());
        assertEquals("bottles washer", employee.getJobTitle());
        assertEquals(Gender.UNSPECIFIED, employee.getGender());
        assertEquals(LocalDate.of(2018, 8, 16), employee.getDateOfBirth());
    }

    @Test
    public void shouldReturnEmptyOptionalWithUnknownEmployeeId() {
        LOGGER.debug("shouldReturnEmptyOptionalWithUnknownEmployeeId()");
        Optional<Employee> optionalEmployee = employeeDao.findById(99);
        assertFalse(optionalEmployee.isPresent());
    }

    @Test
    public void shouldReturnCountOfEmployees() {
        LOGGER.debug("shouldReturnCountOfEmployees()");
        Integer actualCount = employeeDao.findAll().size();
        assertEquals(actualCount, employeeDao.count());
    }

    @Test
    public void shouldSaveNewEmployee() {
        LOGGER.debug("shouldSaveNewEmployee()");
        Integer employeesCountBefore = employeeDao.count();
        Employee newEmployee = getFakeEmployee(128);
        Employee savedEmployee = employeeDao.save(newEmployee);
        assertEquals(4, savedEmployee.getEmployeeId());
        assertEquals(employeesCountBefore + 1, employeeDao.count());
        assertEquals(newEmployee, employeeDao.findById(savedEmployee.getEmployeeId()).orElse(null));
    }

    @Test
    public void shouldUpdateEmployee() {
        LOGGER.debug("shouldUpdateEmployee()");
        Integer employeesCountBefore = employeeDao.count();
        String newJobTitle = "head of bottles washing";
        Integer employeeId = 2;
        Optional<Employee> optionalEmployee = employeeDao.findById(employeeId);
        assertTrue(optionalEmployee.isPresent());

        Employee oldEmployee = optionalEmployee.get();
        oldEmployee.setJobTitle(newJobTitle);
        employeeDao.update(oldEmployee);
        assertEquals(employeesCountBefore, employeeDao.count());
        Optional<Employee> updatedEmployee = employeeDao.findById(employeeId);
        assertTrue(updatedEmployee.isPresent());
        assertEquals(newJobTitle, updatedEmployee.get().getJobTitle());
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");
        Integer employeesCountBefore = employeeDao.count();
        assertTrue(employeeDao.deleteById(2));
        assertEquals(employeesCountBefore - 1, employeeDao.count());
    }

    @Test
    public void shouldReturnTrueWithExistedEmployee() {
        LOGGER.debug("shouldReturnTrueWithExistedEmployee()");
        assertTrue(employeeDao.existsById(2));
    }

    @Test
    public void shouldReturnFalseWithNonExistedEmployee() {
        LOGGER.debug("shouldReturnTrueWithExistedEmployee()");
        assertFalse(employeeDao.existsById(9));
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