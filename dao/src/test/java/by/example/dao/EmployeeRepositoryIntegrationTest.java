package by.example.dao;

import by.example.model.Employee;
import by.example.model.Gender;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sergey Tsynin
 */
@DataJpaTest
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EmployeeRepositoryIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryIntegrationTest.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void shouldReturnEmployeesList() {
        LOGGER.debug("shouldReturnEmployeesList()");
        List<Employee> employees = employeeRepository.findAll();
        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployee() {
        LOGGER.debug("shouldReturnEmployee()");
        Optional<Employee> optionalEmployee = employeeRepository.findById(2);
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
        Optional<Employee> optionalEmployee = employeeRepository.findById(99);
        assertFalse(optionalEmployee.isPresent());
    }

    @Test
    public void shouldReturnCountOfEmployees() {
        LOGGER.debug("shouldReturnCountOfEmployees()");
        Integer actualCount = employeeRepository.findAll().size();
        assertEquals(actualCount, Math.toIntExact(employeeRepository.count()));
    }

    @Test
    public void shouldSaveNewEmployee() {
        LOGGER.debug("shouldSaveNewEmployee()");
        long employeesCountBefore = employeeRepository.count();
        Employee newEmployee = getFakeEmployee(128);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        assertEquals(4, savedEmployee.getEmployeeId());
        assertEquals(employeesCountBefore + 1, employeeRepository.count());
    }

    @Test
    public void shouldUpdateEmployee() {
        LOGGER.debug("shouldUpdateEmployee()");
        long employeesCountBefore = employeeRepository.count();
        String newJobTitle = "head of bottles washers";
        Integer employeeId = 2;
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        assertTrue(optionalEmployee.isPresent());

        Employee oldEmployee = optionalEmployee.get();
        oldEmployee.setJobTitle(newJobTitle);
        Employee savedEmployee = employeeRepository.save(oldEmployee);
        System.out.println(savedEmployee);
        assertEquals(employeesCountBefore, employeeRepository.count());
        Optional<Employee> updatedEmployee = employeeRepository.findById(employeeId);
        assertTrue(updatedEmployee.isPresent());
        assertEquals(newJobTitle, updatedEmployee.get().getJobTitle());
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");
        long employeesCountBefore = employeeRepository.count();
        employeeRepository.deleteById(2);
        assertEquals(employeesCountBefore - 1, employeeRepository.count());
    }

    private Employee getFakeEmployee(Integer id) {
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