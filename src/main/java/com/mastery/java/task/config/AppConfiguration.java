package com.mastery.java.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @author Sergey Tsynin
 */
@Configuration
public class AppConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfiguration.class);

    @Bean
    DataSource dataSource() {
        try {
            DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
            driverManagerDataSource.setUrl("jdbc:postgresql://localhost:5432/employeedb");
            driverManagerDataSource.setUsername("employeedb");
            driverManagerDataSource.setPassword("fakepass");
            LOGGER.debug("DataSource was created");
            return driverManagerDataSource;
        } catch (Exception e) {
            LOGGER.error("DataSource bean cannot be created", e);
            return null;
        }
    }
}