package by.example.controller.rest;

import by.example.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Sergey Tsynin
 */
@Configuration
@EnableJpaRepositories("by.example.dao")
public class TestConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfig.class);

//    @Bean
//    EmployeeRepository employeeRepository(){
//        LOGGER.debug("Mockito.mock(EmployeeRepository.class) was created");
//        return Mockito.mock(EmployeeRepository.class);
//    }

    @Bean
    EmployeeService employeeService() {
        return new EmployeeService();
    }

    @Bean
    EmployeeController employeeController() {
        return new EmployeeController(employeeService());
    }

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

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.TRUE);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("by.example");
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        LOGGER.debug("EntityManagerFactory was created");
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        LOGGER.debug("Create transactionManager");
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory());
        tm.setDataSource(dataSource());
        return tm;
    }
}

