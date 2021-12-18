package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.excepton_handling.NotFoundMasteryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Component
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
                        new NotFoundMasteryException("Employee id: " + employeeId + " was not found in database"));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeJpaRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (!employeeJpaRepository.existsById(employee.getEmployeeId())) {
            throw new NotFoundMasteryException("Employee was not found in database");
        }
        return employeeJpaRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Integer id) {
        if (!employeeJpaRepository.existsById(id)) {
            throw new NotFoundMasteryException("Employee was not found in database");
        }
        employeeJpaRepository.deleteById(id);
    }

    @Override
    public Integer getEmployeesCount() {
        return Math.toIntExact(employeeJpaRepository.count());
    }
}
