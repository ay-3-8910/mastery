package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.rest.excepton_handling.NotFoundMasteryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * @author Sergey Tsynin
 */
public class EmployeeDaoJdbc implements EmployeeDao {

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

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoJdbc.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<Employee> rowMapper = BeanPropertyRowMapper.newInstance(Employee.class);

    @Autowired
    public EmployeeDaoJdbc() {
        LOGGER.debug("Employees DAO JDBC was created");
    }

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void init() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return namedParameterJdbcTemplate.query(
                sqlGetAllEmployee,
                rowMapper);
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        List<Employee> passengers = namedParameterJdbcTemplate.query(
                sqlGetEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id),
                rowMapper);
        Optional<Employee> optionalEmployee = Optional.ofNullable(DataAccessUtils.uniqueResult(passengers));

        if (optionalEmployee.isEmpty()) {
            throw new NotFoundMasteryException(id);
        }
        return optionalEmployee.get();
    }

    @Override
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

    @Override
    public Employee updateEmployee(Employee employee) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(
                sqlUpdateEmployee,
                getParameterSource(employee),
                keyHolder, new String[]{"employee_id"});
        if (keyHolder.getKey() == null) {
            throw new NotFoundMasteryException(employee.getEmployeeId());
        }
        return employee;
    }

    @Override
    public void deleteEmployee(Integer id) {
        int numberOfDeletedEmployees = namedParameterJdbcTemplate.update(
                sqlDeleteEmployeeById,
                new MapSqlParameterSource("EMPLOYEE_ID", id));

        if (numberOfDeletedEmployees == 0) {
            throw new NotFoundMasteryException(id);
        }
    }

    @Override
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
