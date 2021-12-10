package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Sergey Tsynin
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceTest.class);

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    private EmployeeDao employeeDao;

    @Test
    void shouldFindAll() {
        LOGGER.debug("shouldFindAll()");

        // given
        List<Employee> base = new ArrayList<>();
        base.add(getFakeEmployee(1));
        base.add(getFakeEmployee(2));
        base.add(getFakeEmployee(3));
        when(employeeDao.getAllEmployees()).thenReturn(base);

        //when
        List<Employee> employees = employeeService.getAllEmployees();

        //then
        assertEquals(base, employees);
    }

    @Test
    void shouldFindById() {
        LOGGER.debug("shouldFindById()");

        // given
        Employee fakeEmployee = getFakeEmployee(1);
        when(employeeDao.getEmployeeById(1)).thenReturn(fakeEmployee);

        // when
        Employee employee = employeeService.getEmployeeById(1);

        //then
        assertEquals(fakeEmployee, employee);
    }

    @Test
    void shouldReturnEmployeeWithCreateMethod() {
        LOGGER.debug("shouldReturnEmployeeIdWithCreate()");

        // given
        Employee fakeEmployee = getFakeEmployee(22);
        when(employeeDao.createEmployee(fakeEmployee)).thenReturn(fakeEmployee);

        // when
        Employee returnedEmployee = employeeService.createEmployee(fakeEmployee);

        //then
        assertEquals(fakeEmployee, returnedEmployee);
    }

    @Test
    void shouldReturnEmployeeWithUpdateMethod() {
        LOGGER.debug("shouldReturnEmployeeWithUpdateMethod()");

        // given
        Employee fakeEmployee = getFakeEmployee(33);
        when(employeeDao.updateEmployee(fakeEmployee)).thenReturn(fakeEmployee);

        // when
        Employee returnedEmployee = employeeService.updateEmployee(fakeEmployee);

        //then
        assertEquals(fakeEmployee, returnedEmployee);
    }

    @Test
    void shouldReturnTrueIfEmployeeDeleted() {
        LOGGER.debug("shouldReturnTrueIfEmployeeDeleted()");

        // given
        Integer fakeEmployeeId = 22;
        when(employeeDao.deleteEmployee(fakeEmployeeId)).thenReturn(true);

        // when
        boolean isEmployeeDeleted = employeeService.deleteEmployee(fakeEmployeeId);

        //then
        assertTrue(isEmployeeDeleted);
    }

    @Test
    void shouldReturnEmployeesCount() {
        LOGGER.debug("shouldReturnEmployeesCount()");

        // given
        when(employeeDao.getEmployeesCount()).thenReturn(42);

        // when
        Integer employeesCount = employeeService.getEmployeesCount();

        //then
        assertEquals(42, employeesCount);
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