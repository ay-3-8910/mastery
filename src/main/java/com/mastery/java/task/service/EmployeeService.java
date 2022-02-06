package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeJpaRepository;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.excepton_handling.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService() {
        LOGGER.debug("Employees service was created");
    }

    @Autowired
    private EmployeeJpaRepository jpaRepository;

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    public List<Employee> getAllEmployees() {
        return jpaRepository.findAll();
    }

    /**
     * Get employee by employeeId.
     *
     * @param employeeId employee Id.
     * @return employee.
     */
    public Employee getEmployeeById(Integer employeeId) {
        return jpaRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(notFoundForThisIdMessage(employeeId)));
    }

    /**
     * Get employee by firstname and lastname.
     *
     * @param firstName employee firstname.
     * @param lastName  employee lastname.
     * @return employee.
     */
    public List<Employee> getEmployeesByName(String firstName, String lastName) {
        var employees = jpaRepository
                .findByFirstNameContainsAndLastNameContains(firstName, lastName);
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("Nothing was found for these parameters");
        }
        return employees;
    }

    /**
     * Save new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    public Employee createEmployee(Employee employee) {
        return jpaRepository.save(employee);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return updated employee.
     */
    public Employee updateEmployee(Employee employee) {
        Integer employeeId = employee.getEmployeeId();
        Employee employeeToUpdate = jpaRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundForThisIdMessage(employeeId)));

        employeeToUpdate.setFirstName(employee.getFirstName());
        employeeToUpdate.setLastName(employee.getLastName());
        employeeToUpdate.setDepartmentId(employee.getDepartmentId());
        employeeToUpdate.setJobTitle(employee.getJobTitle());
        employeeToUpdate.setGender(employee.getGender());
        employeeToUpdate.setDateOfBirth(employee.getDateOfBirth());

        return jpaRepository.save(employeeToUpdate);
    }

    /**
     * Delete employee by employeeId.
     *
     * @param employeeId employee Id.
     */
    public void deleteEmployee(Integer employeeId) {
        jpaRepository.delete(jpaRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundForThisIdMessage(employeeId))));
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    public Integer getEmployeesCount() {
        return Math.toIntExact(jpaRepository.count());
    }

    private String notFoundForThisIdMessage(Integer employeeId) {
        return String.format("No employee with id %s exists!", employeeId);
    }
}