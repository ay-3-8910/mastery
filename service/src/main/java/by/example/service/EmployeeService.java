package by.example.service;

import by.example.model.Employee;
import by.example.model.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    public List<Employee> getAll() {
        LOGGER.debug("Create fake list");
        ArrayList<Employee> employees = new ArrayList<Employee>();
        employees.add(getFakeEmployee(1));
        employees.add(getFakeEmployee(2));
        employees.add(getFakeEmployee(3));
        LOGGER.debug("Return fake list");
        return employees;
    }

    private Employee getFakeEmployee(Integer id) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName("FirstName" + id);
        employee.setLastName("LastName" + id);
        employee.setDepartmentId(id);
        employee.setJobTitle("JobTitle" + id);
        employee.setGender(Gender.UNSPECIFIED);
        employee.setDateOfBirth(LocalDate.now());
        return employee;
    }
}