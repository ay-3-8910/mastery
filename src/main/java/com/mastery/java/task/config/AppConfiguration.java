package com.mastery.java.task.config;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dao.EmployeeDaoJdbc;
import com.mastery.java.task.dao.EmployeeDaoJpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Sergey Tsynin
 */
@Configuration
public class AppConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String jdbcURl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

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
