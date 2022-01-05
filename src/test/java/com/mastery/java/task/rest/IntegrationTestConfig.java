package com.mastery.java.task.rest;

import com.mastery.java.task.config.AppConfiguration;
import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import javax.sql.DataSource;

/**
 * @author Sergey Tsynin
 */
public class IntegrationTestConfig {

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
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
                return null;
            }

            @Override
            public void commit(TransactionStatus transactionStatus) throws TransactionException {

            }

            @Override
            public void rollback(TransactionStatus transactionStatus) throws TransactionException {

            }
        };
    }

    @Bean
    EmployeeController employeeController() {
        return new EmployeeController();
    }

    @Bean
    EmployeeService employeeService() {
        return new EmployeeService();
    }

    @Bean
    EmployeeDao employeeDao() {
        return new EmployeeDao();
    }

    @Bean
    DataSource dataSource() {
        try {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName(driverClassName);
            driverManagerDataSource.setUrl(jdbcURl);
            driverManagerDataSource.setUsername(dbUsername);
            driverManagerDataSource.setPassword(dbPassword);
            LOGGER.debug("DataSource was created");
            return driverManagerDataSource;
        } catch (Exception e) {
            LOGGER.error("DataSource bean cannot be created", e);
            return null;
        }
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        LOGGER.debug("create NamedParameterJdbcTemplate");
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
