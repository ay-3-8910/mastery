package com.mastery.java.task.config;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dao.EmployeeDaoJdbc;
import com.mastery.java.task.dao.EmployeeDaoJpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Tsynin
 */
@Configuration
public class AppConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

    @Bean
    @ConditionalOnProperty(value = "employee.dao", havingValue = "jdbc")
    public EmployeeDao employeeDaoJdbc() {
        LOGGER.info("DAO JDBC selected");
        return new EmployeeDaoJdbc();
    }

    @Bean
    @ConditionalOnProperty(value = "employee.dao", havingValue = "jpa", matchIfMissing = true)
    public EmployeeDao employeeDaoJpa() {
        LOGGER.info("DAO JPA selected");
        return new EmployeeDaoJpa();
    }
}
