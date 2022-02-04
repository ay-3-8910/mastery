package com.mastery.java.task.rest;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.excepton_handling.IdMismatchException;
import com.mastery.java.task.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "employee", tags = "employee")
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
    @ApiOperation(value = "Get all employees list", tags = "employee")
    @ApiResponse(code = 200, message = "Employees list")
    @GetMapping(produces = {"application/json"})
    public List<Employee> getAllEmployees() {
        LOGGER.info(" IN: getAllEmployees() - []");
        var employees = employeeService.getAllEmployees();
        LOGGER.info("OUT: getAllEmployees() - found {} employee(s)", employees.size());
        return employees;
    }

    /**
     * Get employee by Id.
     *
     * @param id employee Id.
     * @return employee.
     */
    @ApiOperation(value = "Get one employee by id", tags = "employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee"),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public Employee getEmployeeById(@PathVariable @Min(1) Integer id) {
        LOGGER.info(" IN: getEmployeeById() - [{}]", id);
        var employee = employeeService.getEmployeeById(id);
        LOGGER.info("OUT: getEmployeeById() - [{}]", employee);
        return employee;
    }

    /**
     * Get employees by firstname and lastname.
     *
     * @param firstName employee firstname.
     * @param lastName  employee lastname.
     * @return employee.
     */
    @ApiOperation(value = "Get employees by firstname and lastname", tags = "employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employees list"),
            @ApiResponse(code = 404, message = "Employees not found")
    })
    @GetMapping(value = "/search", produces = {"application/json"})
    public List<Employee> getEmployeesByName(@RequestParam(value = "firstName", defaultValue = "") String firstName,
                                             @RequestParam(value = "lastName", defaultValue = "") String lastName) {
        LOGGER.info(" IN: getEmployeeByName() - [{}, {}]", firstName, lastName);
        var employees = employeeService.getEmployeesByName(firstName, lastName);
        LOGGER.info("OUT: getEmployeeByName() - found {} employee(s)", employees.size());
        return employees;
    }

    /**
     * Create new employee record.
     *
     * @param employee object.
     * @return saved employee.
     */
    @ApiOperation(value = "Add a new employee", tags = "employee")
    @ApiResponse(code = 400, message = "Validation error")
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        LOGGER.info(" IN: createEmployee() - [{}]", employee);
        var employeeReturn = employeeService.createEmployee(employee);
        LOGGER.info("OUT: createEmployee() - [{}]", employeeReturn);
        return employeeReturn;
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return updated employee.
     */
    @ApiOperation(value = "Update an existing employee", tags = "employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated employee"),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 404, message = "Employee not found")
    })
    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    public Employee updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
        LOGGER.info(" IN: updateEmployee() - [{}]", employee);
        if (!id.equals(employee.getEmployeeId())) {
            throw new IdMismatchException("Id mismatch");
        }
        var employeeReturn = employeeService.updateEmployee(employee);
        LOGGER.info("OUT: updateEmployee() - [{}]", employeeReturn);
        return employeeReturn;
    }

    /**
     * Delete employee by Id.
     *
     * @param id employee Id.
     */
    @ApiOperation(value = "Delete employee by id", tags = "employee")
    @ApiResponse(code = 404, message = "Employee not found")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer id) {
        LOGGER.info(" IN: deleteEmployee() - [{}]", id);
        employeeService.deleteEmployee(id);
        LOGGER.info("OUT: deleteEmployee() - [{}] - deleted", id);
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    @ApiOperation(value = "Get the number of employees in the database", tags = "employee")
    @GetMapping(value = "/count", produces = {"application/json"})
    public Integer getEmployeesCount() {
        LOGGER.info(" IN: getEmployeesCount() - []");
        var count = employeeService.getEmployeesCount();
        LOGGER.info("OUT: getEmployeesCount() - found {} employee(s)", count);
        return count;
    }
}