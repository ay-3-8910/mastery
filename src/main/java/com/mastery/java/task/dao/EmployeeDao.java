package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * @author Sergey Tsynin
 */
@Repository
public class EmployeeDao {

    @SuppressWarnings("unused")
    @Value("${sqlGetAllEmployee}")
    private String sqlGetAllEmployee;

    @SuppressWarnings("unused")
    @Value("${sqlDeleteEmployeeById}")
    private String sqlDeleteEmployeeById;

    @SuppressWarnings("unused")
    @Value("${sqlGetEmployeesCount}")
    private String sqlGetEmployeesCount;

    @SuppressWarnings("unused")
    @Value("${sqlGetEmployeeById}")
    private String sqlGetEmployeeById;

//    @SuppressWarnings("unused")
//    @Value("${}")
//    private String;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);

    public EmployeeDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Employee> findAll() {
        LOGGER.debug("Employees list request from database");
        List<Employee> employees = namedParameterJdbcTemplate.query(
                sqlGetAllEmployee,
                rowMapper);
        LOGGER.debug("... found {} employee(s)", employees.size());
        return employees;
    }

    public Optional<Employee> findById(Integer id) {
        LOGGER.debug("Get employee id: {} from database", id);
        List<Employee> passengers = namedParameterJdbcTemplate.query(
                sqlGetEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id),
                rowMapper);
        return Optional.ofNullable(DataAccessUtils.uniqueResult(passengers));
    }

    public boolean deleteById(Integer id) {
        LOGGER.debug("Request to delete employee id: {}", id);
        int numberOfDeletedEmployees = namedParameterJdbcTemplate.update(
                sqlDeleteEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id));
        LOGGER.debug("Employees deleted: {}", numberOfDeletedEmployees);
        return numberOfDeletedEmployees > 0;
    }

    public Integer count() {
        LOGGER.debug("Get employees count");
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetEmployeesCount,
                new HashMap<>(),
                Integer.class);
    }
}
