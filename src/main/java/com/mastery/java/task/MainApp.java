package com.mastery.java.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Sergey Tsynin
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.mastery")
@PropertySource({"classpath:sql.properties"})
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}