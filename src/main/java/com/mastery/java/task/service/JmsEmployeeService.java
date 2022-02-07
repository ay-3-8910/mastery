package com.mastery.java.task.service;

import com.mastery.java.task.dao.EmployeeJpaRepository;
import com.mastery.java.task.dto.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author Sergey Tsynin
 */
@Service
public class JmsEmployeeService {

    public JmsEmployeeService() {
        LOGGER.debug("JMS consumer service was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsEmployeeService.class);

    @Autowired
    private EmployeeJpaRepository jpaRepository;

    @JmsListener(destination = "employee-queue")
    public void receiveEmployee(Employee employee) {
        LOGGER.info(" IN: receiveEmployee() - [{}]", employee);
        jpaRepository.save(employee);
    }
}
