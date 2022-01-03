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
import static org.mockito.Mockito.verify;
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
        verify(employeeDao).getAllEmployees();
    }

    @Test
    void shouldFindById() {
        LOGGER.debug("shouldFindById()");

        // given
        Integer interactionEmployeeId = 1;
        Employee fakeEmployee = getFakeEmployee(interactionEmployeeId);
        when(employeeDao.getEmployeeById(1)).thenReturn(fakeEmployee);

        // when
        Employee employee = employeeService.getEmployeeById(interactionEmployeeId);

        //then
        assertEquals(fakeEmployee, employee);
        verify(employeeDao).getEmployeeById(interactionEmployeeId);
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
        verify(employeeDao).createEmployee(fakeEmployee);
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
        verify(employeeDao).updateEmployee(fakeEmployee);
    }

    @Test
    void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");

        // given
        Integer interactionEmployeeId = 123;

        // when
        employeeService.deleteEmployee(interactionEmployeeId);

        // then
        verify(employeeDao).deleteEmployee(interactionEmployeeId);
    }

    @Test
    void shouldReturnEmployeesCount() {
        LOGGER.debug("shouldReturnEmployeesCount()");

        // given
        Integer interactionEmployeeId = 42;
        when(employeeDao.getEmployeesCount()).thenReturn(interactionEmployeeId);

        // when
        Integer employeesCount = employeeService.getEmployeesCount();

        //then
        assertEquals(42, employeesCount);
        verify(employeeDao).getEmployeesCount();
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