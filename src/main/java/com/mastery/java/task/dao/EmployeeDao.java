package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;


/**
 * @author Sergey Tsynin
 */
@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);

    public EmployeeDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Employee> findAll() {
        LOGGER.debug("Employees list request from database");
        String sqlGetAllEmployee = "SELECT * FROM EMPLOYEES AS E ORDER BY E.EMPLOYEE_ID";
        List<Employee> employees = namedParameterJdbcTemplate.query(
                sqlGetAllEmployee,
                rowMapper);
        LOGGER.debug("... found {} employee(s)", employees.size());
        return employees;
    }

    public boolean deleteById(Integer id) {
        LOGGER.debug("Request to delete employee id: {}", id);
        String sqlDeleteEmployeeById = "DELETE FROM EMPLOYEES WHERE EMPLOYEE_ID = :EMPLOYEE_ID";
        int numberOfDeletedEmployees = namedParameterJdbcTemplate.update(
                sqlDeleteEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id));
        LOGGER.debug("Employees deleted: {}", numberOfDeletedEmployees);
        return numberOfDeletedEmployees > 0;
    }

    public Integer count() {
        LOGGER.debug("Get employees count");
        String sqlGetEmployeesCount = "SELECT COUNT(*) FROM EMPLOYEES";
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetEmployeesCount,
                new HashMap<>(),
                Integer.class);
    }
}
