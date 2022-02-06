package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeJpaRepository;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.rest.excepton_handling.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    EmployeeJpaRepository jpaRepository;

    @Test
    void shouldFindAll() {
        LOGGER.debug("shouldFindAll()");

        // given
        List<Employee> base = new ArrayList<>();
        base.add(getFakeEmployee(1));
        base.add(getFakeEmployee(2));
        base.add(getFakeEmployee(3));
        when(jpaRepository.findAll()).thenReturn(base);

        // when
        List<Employee> employees = employeeService.getAllEmployees();

        // then
        assertEquals(base, employees);
        verify(jpaRepository).findAll();
    }

    @Test
    void shouldFindById() {
        LOGGER.debug("shouldFindById()");

        // given
        Employee fakeEmployee = getFakeEmployee(1);
        when(jpaRepository.findById(1)).thenReturn(Optional.of(fakeEmployee));

        // when
        Employee employee = employeeService.getEmployeeById(1);

        // then
        assertEquals(fakeEmployee, employee);
        verify(jpaRepository).findById(1);
    }

    @Test
    public void shouldReturnExceptionWithUnknownEmployeeId() {
        LOGGER.debug("shouldReturnExceptionWithUnknownEmployeeId()");

        // given
        when(jpaRepository.findById(99)).thenReturn(Optional.empty());

        // then
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(99));
        assertEquals("No employee with id 99 exists!", exception.getMessage());
        verify(jpaRepository).findById(99);
    }

    @Test
    void shouldFindByName() {
        LOGGER.debug("shouldFindByName()");

        // given
        List<Employee> base = new ArrayList<>();
        base.add(getFakeEmployee(1));
        when(jpaRepository.findByFirstNameContainsAndLastNameContains("Ali", "Baba")).thenReturn(base);

        // when
        List<Employee> employees = employeeService.getEmployeesByName("Ali", "Baba");

        // then
        assertEquals(base, employees);
        verify(jpaRepository).findByFirstNameContainsAndLastNameContains("Ali", "Baba");
    }

    @Test
    public void shouldReturnExceptionWithUnknownEmployeeName() {
        LOGGER.debug("shouldReturnExceptionWithUnknownEmployeeName()");

        // given
        when(jpaRepository.findByFirstNameContainsAndLastNameContains("None", "None")).thenReturn(new ArrayList<>());

        // then
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeesByName("None", "None"));
        assertEquals("Nothing was found for these parameters", exception.getMessage());
        verify(jpaRepository).findByFirstNameContainsAndLastNameContains("None", "None");
    }

    @Test
    void shouldReturnEmployeeWithCreateMethod() {
        LOGGER.debug("shouldReturnEmployeeIdWithCreate()");

        // given
        Employee fakeEmployee = getFakeEmployee(22);
        when(jpaRepository.save(fakeEmployee)).thenReturn(fakeEmployee);

        // when
        Employee returnedEmployee = employeeService.createEmployee(fakeEmployee);

        // then
        assertEquals(fakeEmployee, returnedEmployee);
        verify(jpaRepository).save(fakeEmployee);
    }

    @Test
    void shouldReturnEmployeeWithUpdateMethod() {
        LOGGER.debug("shouldReturnEmployeeWithUpdateMethod()");

        // given
        Employee employeeToUpdate = getFakeEmployee(33);
        Employee newEmployeeInfo = getFakeEmployee(42);
        newEmployeeInfo.setEmployeeId(33);
        when(jpaRepository.findById(33)).thenReturn(Optional.of(employeeToUpdate));
        when(jpaRepository.save(any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        Employee returnedEmployee = employeeService.updateEmployee(newEmployeeInfo);

        // then
        assertEquals(newEmployeeInfo, returnedEmployee);
        verify(jpaRepository).findById(33);
        verify(jpaRepository).save(any());
    }

    @Test
    void shouldReturnExceptionWithUpdateUnknownEmployeeId() {
        LOGGER.debug("shouldReturnExceptionWithUpdateUnknownEmployeeId()");

        // given
        Employee fakeEmployee = getFakeEmployee(99);
        when(jpaRepository.findById(99)).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.updateEmployee(fakeEmployee));

        // then
        assertEquals("No employee with id 99 exists!", exception.getMessage());
        verify(jpaRepository).findById(99);
    }

    @Test
    public void shouldDeleteEmployee() {
        LOGGER.debug("shouldDeleteEmployee()");

        // given
        Employee fakeEmployee = getFakeEmployee(1);
        when(jpaRepository.findById(1)).thenReturn(Optional.of(fakeEmployee));

        // when
        employeeService.deleteEmployee(1);

        // then
        verify(jpaRepository).findById(1);
        verify(jpaRepository).delete(fakeEmployee);
    }

    @Test
    public void shouldReturnExceptionIfDeleteEmployeeWithUnknownId() {
        LOGGER.debug("shouldReturnExceptionIfDeleteEmployeeWithUnknownId()");

        // given
        when(jpaRepository.findById(9)).thenReturn(Optional.empty());

        // then
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteEmployee(9));
        assertEquals("No employee with id 9 exists!", exception.getMessage());
        verify(jpaRepository).findById(9);
    }

    @Test
    void shouldReturnEmployeesCount() {
        LOGGER.debug("shouldReturnEmployeesCount()");

        // given
        when(jpaRepository.count()).thenReturn(42L);

        // when
        Integer employeesCount = employeeService.getEmployeesCount();

        // then
        assertEquals(42, employeesCount);
        verify(jpaRepository).count();
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