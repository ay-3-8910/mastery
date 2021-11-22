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
import java.util.Optional;

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
        Optional<Employee> optionalEmployee = employeeService.getById(id);
        LOGGER.debug("Return employee id: {}", id);
        return new ResponseEntity<>(optionalEmployee.get(), HttpStatus.OK);
    }

    /**
     * Create new employee record.
     *
     * @param employee object.
     * @return saved employee Id.
     */
    @PostMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> create(@Valid @RequestBody Employee employee) {
        LOGGER.debug("Request to create new employee");
        if (employeeFieldsIsCorrect(employee, "Create")) {
            return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
        }
        LOGGER.error("Return creating error");
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return equivalent HttpStatus and empty body.
     */
    @PutMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Void> update(@RequestBody Employee employee) {
        Integer id = employee.getEmployeeId();
        LOGGER.debug("Request to update employee id: {} ", id);
        if (!employeeFieldsIsCorrect(employee, "Update")) {
            LOGGER.error("Return updating error");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (employeeService.updateEmployee(employee)) {
            LOGGER.debug("Return result - employee with id: {} updated", id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        LOGGER.debug("Return result - employee with id: {} was not updated because it was not found", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
        if (employeeService.deleteEmployee(id)) {
            LOGGER.debug("Return result - employee with id: {} deleted", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        LOGGER.debug("Return result - employee with id: {} was not deleted because it was not found", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    private boolean employeeFieldsIsCorrect(Employee employee, String stage) {
        LOGGER.debug("Checking fields for correctness");
        if (employee.getFirstName() == null) {
            LOGGER.error(stage + " fail. Employee firstname is null");
            return false;
        }
        if (employee.getLastName() == null) {
            LOGGER.error(stage + " fail. Employee lastname is null");
            return false;
        }
        LOGGER.debug("Fields are ok");
        return true;
    }
}