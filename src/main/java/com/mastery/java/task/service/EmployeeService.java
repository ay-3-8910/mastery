package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
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

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    public List<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    /**
     * Get employee by Id.
     *
     * @param employeeId train Id.
     * @return employee.
     */
    public Employee getEmployeeById(Integer employeeId) {
        return employeeDao.getEmployeeById(employeeId);
    }

    /**
     * Save new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    public Employee createEmployee(Employee employee) {
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
        return employeeDao.updateEmployee(employee);
    }

    /**
     * Delete employee by Id.
     *
     * @param employeeId employee Id.
     * @return has employee deleted.
     */
    public boolean deleteEmployee(Integer employeeId) {
        return employeeDao.deleteEmployee(employeeId);
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    public Integer getEmployeesCount() {
        return Math.toIntExact(employeeDao.getEmployeesCount());
    }
}