package com.mastery.java.task.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
}
