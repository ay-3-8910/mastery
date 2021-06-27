package by.example.controller.rest;

import by.example.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@RestController
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    @GetMapping(value = "/employees", produces = {"application/json"})
    public final ResponseEntity<List<Employee>> getAll() {
        LOGGER.debug("Employees list request");
        return new ResponseEntity<>(
                //TODO get real list
                new ArrayList<Employee>(),
                HttpStatus.OK);
    }
}