package by.example.service;

import by.example.model.Employee;
import by.example.model.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(getFakeEmployee(1));
        employees.add(getFakeEmployee(2));
        employees.add(getFakeEmployee(3));
        LOGGER.debug("Return fake list");
        return employees;
    }

    /**
     * Get employee by Id.
     *
     * @param employeeId train Id.
     * @return employee.
     */
    public Optional<Employee> getById(Integer employeeId) {
        LOGGER.debug("Get employee id: {}", employeeId);
        return Optional.empty();
    }

    /**
     * Save new employee record.
     *
     * @param employee object.
     * @return saved employee Id.
     */
    public Integer createEmployee(Employee employee) {
        LOGGER.debug("Create employee");
        return 0;
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return number of updated employees in the database.
     */
    public Integer updateEmployee(Employee employee) {
        LOGGER.debug("Update employee");
        return 0;
    }

    /**
     * Delete employee by Id.
     *
     * @param employeeId employee Id.
     * @return number of deleted employees in the database.
     */
    public Integer deleteEmployee(Integer employeeId) {
        LOGGER.debug("Delete employee");
        return 0;
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    public Integer getEmployeesCount() {
        LOGGER.debug("Get employees count");
        return 0;
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