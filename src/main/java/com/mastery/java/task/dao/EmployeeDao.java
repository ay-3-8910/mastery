package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
public interface EmployeeDao {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Integer id);

    List<Employee> getEmployeesByName(String firstName, String lastName);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Integer id);

    Integer getEmployeesCount();
}
