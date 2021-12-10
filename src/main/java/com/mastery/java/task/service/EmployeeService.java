package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Service
@Validated
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    public EmployeeService() {
        LOGGER.debug("Employees service was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    public List<Employee> getAllEmployees() {
        LOGGER.debug("Employees list request from repository");
        return employeeDao.getAllEmployees();
    }

    /**
     * Get employee by Id.
     *
     * @param employeeId train Id.
     * @return employee.
     */
    public Employee getEmployeeById(Integer employeeId) {
        LOGGER.debug("Get employee id: {} from repository", employeeId);
        return employeeDao.getEmployeeById(employeeId);
    }

    /**
     * Save new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    public Employee createEmployee(Employee employee) {
        LOGGER.debug("Save employee into repository");
        return employeeDao.createEmployee(employee);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return updated employee.
     */
    public Employee updateEmployee(Employee employee) {
        Integer id = employee.getEmployeeId();
        LOGGER.debug("Request to update employee id: {}", id);
        return employeeDao.updateEmployee(employee);
    }

    /**
     * Delete employee by Id.
     *
     * @param employeeId employee Id.
     * @return has employee deleted.
     */
    public boolean deleteEmployee(Integer employeeId) {
        LOGGER.debug("Request to delete employee id: {}", employeeId);
        return employeeDao.deleteEmployee(employeeId);
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    public Integer getEmployeesCount() {
        LOGGER.debug("Get employees count");
        return Math.toIntExact(employeeDao.getEmployeesCount());
    }

    /**
     * Check if employee exists in the database.
     *
     * @param employeeId employee Id.
     */
    public boolean isEmployeeExists(Integer employeeId) {
        LOGGER.debug("Check if employee exists");
        return employeeDao.existsById(employeeId);
    }
}