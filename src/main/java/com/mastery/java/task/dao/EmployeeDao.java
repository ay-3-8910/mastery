package com.mastery.java.task.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;


/**
 * @author Sergey Tsynin
 */
@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EmployeeDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Integer count() {
        LOGGER.debug("Get employees count");
        String sqlGetEmployeesCount = "SELECT COUNT(*) FROM EMPLOYEES";
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetEmployeesCount,
                new HashMap<>(),
                Integer.class);
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
}
