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

        //when
        when(employeeDao.findAll()).thenReturn(base);

        //then
        List<Employee> employees = employeeService.findAll();
        System.out.println(employees);
        assertEquals(base, employees);
    }

    @Test
    void shouldFindById() {
        LOGGER.debug("shouldFindById()");

        // given
        Employee fakeEmployee = getFakeEmployee(1);

        // when
        when(employeeDao.findById(1)).thenReturn(Optional.of(fakeEmployee));

        //then
        Optional<Employee> employee = employeeService.getById(1);
        assertTrue(employee.isPresent());
        assertEquals(fakeEmployee, employee.get());
    }

    @Test
    void shouldReturnEmployeeIdWithCreate() {
        LOGGER.debug("shouldReturnEmployeeIdWithCreate()");

        // given
        Employee fakeEmployee = getFakeEmployee(22);

        // when
        when(employeeDao.save(fakeEmployee)).thenReturn(fakeEmployee);

        //then
        assertEquals(22, employeeService.createEmployee(fakeEmployee));
    }

    @Test
    void shouldReturnTrueWithUpdateEmployee() {
        LOGGER.debug("shouldReturnTrueWithUpdateEmployee()");

        // given
        Employee fakeEmployee = getFakeEmployee(22);

        // when
        when(employeeDao.update(fakeEmployee)).thenReturn(fakeEmployee);
        when(employeeDao.existsById(22)).thenReturn(true);

        //then
        assertEquals(true, employeeService.updateEmployee(fakeEmployee));
    }

    @Test
    void shouldReturnFalseWithUpdateNonExistsEmployee() {
        LOGGER.debug("shouldReturnFalseWithUpdateNonExistsEmployee()");

        // given
        Employee fakeEmployee = getFakeEmployee(33);

        // when
        when(employeeDao.existsById(33)).thenReturn(false);

        //then
        assertEquals(false, employeeService.updateEmployee(fakeEmployee));
    }

    @Test
    void shouldReturnTrueIfEmployeeDeleted() {
        LOGGER.debug("shouldReturnTrueIfEmployeeDeleted()");

        // given
        Integer fakeEmployeeId = 22;

        // when
        when(employeeDao.deleteById(fakeEmployeeId)).thenReturn(true);
        when(employeeDao.existsById(fakeEmployeeId)).thenReturn(true);

        //then
        assertTrue(employeeService.deleteEmployee(fakeEmployeeId));
    }

    @Test
    void shouldReturnFalseWithDeleteNonExistsEmployee() {
        LOGGER.debug("shouldReturnFalseWithUpdateNonExistsEmployee()");

        // given
        Integer fakeEmployeeId = 22;

        // when
        when(employeeDao.existsById(fakeEmployeeId)).thenReturn(false);

        //then
        assertFalse(employeeService.deleteEmployee(fakeEmployeeId));
    }


    @Test
    void shouldReturnEmployeesCount() {
        LOGGER.debug("shouldReturnEmployeesCount()");

        // when
        when(employeeDao.count()).thenReturn(42);

        //then
        assertEquals(42, employeeService.getEmployeesCount());
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