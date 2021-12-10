package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.excepton_handling.NotFoundMasteryException;
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
        LOGGER.debug("Employees DAO was created");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Employee> getAllEmployees() {
        return namedParameterJdbcTemplate.query(
                sqlGetAllEmployee,
                rowMapper);
    }

    public Employee getEmployeeById(Integer id) {
        List<Employee> passengers = namedParameterJdbcTemplate.query(
                sqlGetEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id),
                rowMapper);
        Optional<Employee> optionalEmployee = Optional.ofNullable(DataAccessUtils.uniqueResult(passengers));

        if (optionalEmployee.isEmpty()) {
            throw new NotFoundMasteryException("Employee id: " + id + " was not found in database");
        }
        return optionalEmployee.get();
    }

    public Employee createEmployee(Employee employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sqlCreateEmployee,
                getParameterSource(employee),
                keyHolder, new String[]{"employee_id"});
        Integer newEmployeeId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        employee.setEmployeeId(newEmployeeId);
        return employee;
    }

    public Employee updateEmployee(Employee employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sqlUpdateEmployee,
                getParameterSource(employee),
                keyHolder, new String[]{"employee_id"});
        if (keyHolder.getKey() == null) {
            throw new NotFoundMasteryException("Employee was not found in database");
        }
        return employee;
    }

    public void deleteEmployee(Integer id) {
        int numberOfDeletedEmployees = namedParameterJdbcTemplate.update(
                sqlDeleteEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id));

        if (numberOfDeletedEmployees == 0) {
            throw new NotFoundMasteryException("Employee id: " + id + " was not found in database");
        }
    }

    public Integer getEmployeesCount() {
        return namedParameterJdbcTemplate.queryForObject(
                sqlGetEmployeesCount,
                new HashMap<>(),
                Integer.class);
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
