package com.mastery.java.task.dao;

import com.mastery.java.task.config.AppConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Sergey Tsynin
 */
@DataJdbcTest
@Import(EmployeeDao.class)
@ContextConfiguration(classes = AppConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EmployeeDaoTest {

    @Autowired
    EmployeeDao employeeDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDaoTest.class);

    @Test
    void count() {
        LOGGER.debug("fake count test()");
        Integer employees = employeeDao.count();
        assertNotNull(employees);
        assertEquals(3, employees);
    }

//    @Test
//    public void shouldReturnCountOfEmployees() {
//        LOGGER.debug("shouldReturnCountOfEmployees()");
//        Integer actualCount = employeeDao.findAll().size();
//        assertEquals(actualCount, Math.toIntExact(employeeDao.count()));
//    }
}