package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
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
    private EmployeeDao employeeDao;

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
        return employeeDao.findAll();
    }

    /**
     * Get employee by Id.
     *
     * @param employeeId train Id.
     * @return employee.
     */
    public Optional<Employee> getById(Integer employeeId) {
        LOGGER.debug("Get employee id: {} from repository", employeeId);
        return employeeDao.findById(employeeId);
    }

    /**
     * Save new employee record.
     *
     * @param employee object.
     * @return saved employee Id.
     */
    public Integer createEmployee(Employee employee) {
        LOGGER.debug("Save employee into repository");
        return employeeDao.save(employee).getEmployeeId();
    }

    /**
     * Update employee record in the database.
     *
     * @param employee object.
     * @return has employee updated.
     */
    public Boolean updateEmployee(Employee employee) {
        Integer id = employee.getEmployeeId();
        LOGGER.debug("Request to update employee id: {}", id);
        if (employeeDao.existsById(id)) {
            LOGGER.debug("updating employee");
            return employee.equals(employeeDao.update(employee));
        }
        LOGGER.error("...but employee not found for update!");
        return false;
    }

    /**
     * Delete employee by Id.
     *
     * @param employeeId employee Id.
     * @return has employee deleted.
     */
    public boolean deleteEmployee(Integer employeeId) {
        LOGGER.debug("Request to delete employee id: {}", employeeId);
        if (employeeDao.existsById(employeeId)) {
            LOGGER.debug("deleting employee");
            return employeeDao.deleteById(employeeId);
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
        return Math.toIntExact(employeeDao.count());
    }
}