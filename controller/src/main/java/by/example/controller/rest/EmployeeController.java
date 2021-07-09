package by.example.controller.rest;

import by.example.model.Employee;
import by.example.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (optionalEmployee.isEmpty()) {
            LOGGER.error("Employee not found for id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.debug("return employee id: {}", id);
        return new ResponseEntity<>(optionalEmployee.get(), HttpStatus.OK);
    }

    /**
     * Create new employee record.
     *
     * @param employee object.
     * @return saved employee Id.
     */
    @PostMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> create(@RequestBody Employee employee) {
        LOGGER.debug("Request to create new employee");
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return number of updated employees in the database.
     */
    @PutMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public final ResponseEntity<Integer> update(@RequestBody Employee employee) {
        LOGGER.debug("Request to update employee");
        return new ResponseEntity<>(employeeService.updateEmployee(employee), HttpStatus.OK);
    }

    /**
     * Delete employee by Id.
     *
     * @param id employee Id.
     * @return number of deleted employees in the database.
     */
    @DeleteMapping(value = "/employees/{id}", produces = {"application/json"})
    public final ResponseEntity<Integer> delete(@PathVariable Integer id) {
        LOGGER.debug("Request to delete employee id: {} ", id);
        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.NO_CONTENT);
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