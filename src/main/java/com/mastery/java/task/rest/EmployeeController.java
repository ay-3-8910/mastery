package com.mastery.java.task.rest;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
@RequestMapping("employees")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        LOGGER.debug("Employees controller was created");
    }

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    @GetMapping(value = "", produces = {"application/json"})
    public final List<Employee> getAll() {
        LOGGER.debug("Employees list request from service");
        return employeeService.findAll();
    }

    /**
     * Get employee by Id.
     *
     * @param id employee Id.
     * @return employee.
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public final Employee getById(@PathVariable Integer id) {
        LOGGER.debug("Employee id: {} request from service", id);
        Employee employee = employeeService.getById(id);
        LOGGER.debug("Return employee id: {}", id); //todo return employee
        return employee;
    }

    /**
     * Create new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    @PostMapping(value = "", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public final Employee create(@Valid @RequestBody Employee employee) {
        LOGGER.debug("Request to create new employee");
        return employeeService.createEmployee(employee);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return updated employee.
     */
    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public final Employee update(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        LOGGER.debug("Request to update employee id: {} ", id);
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        LOGGER.debug("Return result - employee with id: {} updated", id);
        return updatedEmployee;
    }

    /**
     * Delete employee by Id.
     *
     * @param id employee Id.
     */
    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void delete(@PathVariable Integer id) {
        LOGGER.debug("Request to delete employee id: {} ", id);
        employeeService.deleteEmployee(id);
        LOGGER.debug("Return result - employee with id: {} deleted", id);
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    @GetMapping(value = "/count", produces = {"application/json"})
    public final Integer count() {
        LOGGER.debug("Request to get count of employees");
        return employeeService.getEmployeesCount();
    }
}