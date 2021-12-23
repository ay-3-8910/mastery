package com.mastery.java.task.rest;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Validated
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
    @GetMapping(produces = {"application/json"})
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Get employee by Id.
     *
     * @param id employee Id.
     * @return employee.
     */
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public Employee getEmployeeById(@PathVariable @Min(1) Integer id) {
        return employeeService.getEmployeeById(id);
    }

    /**
     * Create new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return updated employee.
     */
    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public Employee updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    /**
     * Delete employee by Id.
     *
     * @param id employee Id.
     */
    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    @GetMapping(value = "/count", produces = {"application/json"})
    public Integer getEmployeesCount() {
        return employeeService.getEmployeesCount();
    }
}