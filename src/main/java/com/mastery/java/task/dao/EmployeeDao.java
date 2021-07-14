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
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

    @SuppressWarnings("unused")
    @Value("${sqlCreateEmployee}")
    private String sqlCreateEmployee;

    @SuppressWarnings("unused")
    @Value("${sqlUpdateEmployee}")
    private String sqlUpdateEmployee;

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
        LOGGER.debug("Numbers of deleted employees: {}", numberOfDeletedEmployees);
        return numberOfDeletedEmployees > 0;
    }

    public Integer count() {
        LOGGER.debug("Get employees count from database");
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetEmployeesCount,
                new HashMap<>(),
                Integer.class);
    }

    public Employee save(Employee employee) {
        LOGGER.debug("Save employee into database");
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sqlCreateEmployee,
                getParameterSource(employee),
                keyHolder, new String[]{"employee_id"});
        Integer newEmployeeId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        employee.setEmployeeId(newEmployeeId);
        LOGGER.debug("New employee was created with id: {}", newEmployeeId);
        LOGGER.debug("{}", employee);
        return employee;
    }

    public Employee update(Employee employee) {
        LOGGER.debug("Update employee in database");
        namedParameterJdbcTemplate.update(
                sqlUpdateEmployee,
                getParameterSource(employee));
        return employee;
    }

    private SqlParameterSource getParameterSource(Employee employee) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.
                addValue("EMPLOYEE_ID", employee.getEmployeeId()).
                addValue("FIRST_NAME", employee.getFirstName()).
                addValue("LAST_NAME", employee.getLastName()).
                addValue("DEPARTMENT_ID", employee.getDepartmentId()).
                addValue("JOB_TITLE", employee.getJobTitle()).
                addValue("GENDER", employee.getGender().name()).
                addValue("DATE_OF_BIRTH", employee.getDateOfBirth());
        return parameterSource;
    }
}
