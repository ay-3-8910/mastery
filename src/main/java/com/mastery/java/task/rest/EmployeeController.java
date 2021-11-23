package com.mastery.java.task.rest;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
        LOGGER.debug("Employees controller was created");
    }

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    @GetMapping(value = "/employees", produces = {"application/json"})
    public final ResponseEntity<List<Employee>> getAll() {
        LOGGER.debug("Employees list request from service");
        return new ResponseEntity<>(
                employeeService.findAll(),
                HttpStatus.OK);
    }

    /**
     * Get employee by Id.
     *
     * @param id employee Id.
     * @return employee.
     */
    @GetMapping(value = "/employees/{id}", produces = {"application/json"})
    public final ResponseEntity<Employee> getById(@PathVariable Integer id) {
        LOGGER.debug("Employee id: {} request from service", id);
        Employee employee = employeeService.getById(id);
        LOGGER.debug("Return employee id: {}", id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
     * Create new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    @PostMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> create(@Valid @RequestBody Employee employee) {
        LOGGER.debug("Request to create new employee");
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return equivalent HttpStatus and empty body.
     */
    @PutMapping(value = "/employees/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        LOGGER.debug("Request to update employee id: {} ", id);

        if (employeeService.isEmployeeExists(id)) {
            LOGGER.debug("Execute update");
            employeeService.updateEmployee(employee);
            LOGGER.debug("Return result - employee with id: {} updated", id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        LOGGER.debug("Redirect to create new employee");
        LOGGER.warn("Id: {} was not found in database, creating new employee", id);
        employeeService.createEmployee(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete employee by Id.
     *
     * @param id employee Id.
     * @return equivalent HttpStatus and empty body.
     */
    @DeleteMapping(value = "/employees/{id}", produces = {"application/json"})
    public final ResponseEntity<Void> delete(@PathVariable Integer id) {
        LOGGER.debug("Request to delete employee id: {} ", id);
        employeeService.deleteEmployee(id);
        LOGGER.debug("Return result - employee with id: {} deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    @GetMapping(value = "/employees/count", produces = {"application/json"})
    public final ResponseEntity<Integer> count() {
        LOGGER.debug("Request to get count of employees");
        return new ResponseEntity<>(employeeService.getEmployeesCount(), HttpStatus.OK);
    }
}