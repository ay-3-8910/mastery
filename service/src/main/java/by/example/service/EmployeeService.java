package by.example.service;

import by.example.dao.EmployeeRepository;
import by.example.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Sergey Tsynin
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeService() {
        LOGGER.debug("Employees service was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    /**
     * Employees list.
     *
     * @return Employees list.
     */
    public List<Employee> findAll() {
        LOGGER.debug("Employees list request from repository");
        return employeeRepository.findAll();
    }

    /**
     * Get employee by Id.
     *
     * @param employeeId train Id.
     * @return employee.
     */
    public Optional<Employee> getById(Integer employeeId) {
        LOGGER.debug("Get employee id: {} from repository", employeeId);
        return employeeRepository.findById(employeeId);
    }

    /**
     * Save new employee record.
     *
     * @param employee object.
     * @return saved employee Id.
     */
    public Integer createEmployee(Employee employee) {
        LOGGER.debug("Save employee into repository");
        return employeeRepository.save(employee).getEmployeeId();
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return number of updated employees in the database.
     */
    public Integer updateEmployee(Employee employee) {
        LOGGER.debug("Update employee");
        employeeRepository.save(employee);
        return 1;
    }

    /**
     * Delete employee by Id.
     *
     * @param employeeId employee Id.
     * @return has employee deleted.
     */
    public boolean deleteEmployee(Integer employeeId) {
        LOGGER.debug("Request to delete employee id: {}", employeeId);
        if (employeeRepository.existsById(employeeId)) {
            LOGGER.debug("deleting employee");
            employeeRepository.deleteById(employeeId);
            return true;
        }
        LOGGER.error("...but employee not found for delete!");
        return false;
    }

    /**
     * Get the number of employees in the database.
     *
     * @return the number of employees in the database.
     */
    public Integer getEmployeesCount() {
        LOGGER.debug("Get employees count");
        return Math.toIntExact(employeeRepository.count());
    }
}