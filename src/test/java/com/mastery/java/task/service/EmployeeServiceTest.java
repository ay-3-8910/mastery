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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        when(employeeDao.findAll()).thenReturn(base);

        //when
        List<Employee> employees = employeeService.findAll();

        //then
        assertEquals(base, employees);
    }

    @Test
    void shouldFindById() {
        LOGGER.debug("shouldFindById()");

        // given
        Employee fakeEmployee = getFakeEmployee(1);
        when(employeeDao.findById(1)).thenReturn(Optional.of(fakeEmployee));

        // when
        Optional<Employee> employee = employeeService.getById(1);

        //then
        assertTrue(employee.isPresent());
        assertEquals(fakeEmployee, employee.get());
    }

    @Test
    void shouldReturnEmployeeIdWithCreate() {
        LOGGER.debug("shouldReturnEmployeeIdWithCreate()");

        // given
        Employee fakeEmployee = getFakeEmployee(22);
        when(employeeDao.save(fakeEmployee)).thenReturn(fakeEmployee);

        // when
        Integer newFakeEmployeeId = employeeService.createEmployee(fakeEmployee);

        //then
        assertEquals(22, newFakeEmployeeId);
    }

    @Test
    void shouldReturnTrueWithUpdateEmployee() {
        LOGGER.debug("shouldReturnTrueWithUpdateEmployee()");

        // given
        Employee fakeEmployee = getFakeEmployee(22);
        when(employeeDao.update(fakeEmployee)).thenReturn(fakeEmployee);
        when(employeeDao.existsById(22)).thenReturn(true);

        // when
        boolean isEmployeeUpdated = employeeService.updateEmployee(fakeEmployee);

        //then
        assertTrue(isEmployeeUpdated);
    }

    @Test
    void shouldReturnFalseWithUpdateNonExistsEmployee() {
        LOGGER.debug("shouldReturnFalseWithUpdateNonExistsEmployee()");

        // given
        Employee fakeEmployee = getFakeEmployee(33);
        when(employeeDao.existsById(33)).thenReturn(false);

        // when
        boolean isEmployeeUpdated = employeeService.updateEmployee(fakeEmployee);

        //then
        assertFalse(isEmployeeUpdated);
    }

    @Test
    void shouldReturnTrueIfEmployeeDeleted() {
        LOGGER.debug("shouldReturnTrueIfEmployeeDeleted()");

        // given
        Integer fakeEmployeeId = 22;
        when(employeeDao.deleteById(fakeEmployeeId)).thenReturn(true);
        when(employeeDao.existsById(fakeEmployeeId)).thenReturn(true);

        // when
        boolean isEmployeeDeleted = employeeService.deleteEmployee(fakeEmployeeId);

        //then
        assertTrue(isEmployeeDeleted);
    }

    @Test
    void shouldReturnFalseWithDeleteNonExistsEmployee() {
        LOGGER.debug("shouldReturnFalseWithUpdateNonExistsEmployee()");

        // given
        Integer fakeEmployeeId = 22;
        when(employeeDao.existsById(fakeEmployeeId)).thenReturn(false);

        // when
        boolean isEmployeeDeleted = employeeService.deleteEmployee(fakeEmployeeId);

        //then
        assertFalse(isEmployeeDeleted);
    }


    @Test
    void shouldReturnEmployeesCount() {
        LOGGER.debug("shouldReturnEmployeesCount()");

        // given
        when(employeeDao.count()).thenReturn(42);

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