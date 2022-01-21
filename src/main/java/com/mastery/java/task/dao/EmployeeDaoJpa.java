package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.excepton_handling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
public class EmployeeDaoJpa implements EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoJpa.class);

    public EmployeeDaoJpa() {
        LOGGER.debug("Employees DAO JPA was created");
    }

    @Autowired
    EmployeeJpaRepository employeeJpaRepository;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeJpaRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Integer employeeId) {
        return employeeJpaRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(notFoundForThisIdMessage(employeeId)));
    }

    @Override
    public List<Employee> getEmployeesByName(String firstName, String lastName) {
        var employees = employeeJpaRepository
                .findByFirstNameContainsAndLastNameContains(firstName, lastName);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("Nothing was found for these parameters");
        }
        return employees;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeJpaRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Integer employeeId = employee.getEmployeeId();
        if (!employeeJpaRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(notFoundForThisIdMessage(employeeId));
        }
        return employeeJpaRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        if (!employeeJpaRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException(notFoundForThisIdMessage(employeeId));
        }
        employeeJpaRepository.deleteById(employeeId);
    }

    @Override
    public Integer getEmployeesCount() {
        return Math.toIntExact(employeeJpaRepository.count());
    }

    private String notFoundForThisIdMessage(Integer employeeId) {
        return "Employee id: " + employeeId + " was not found in database";
    }
}
