package com.mastery.java.task.rest;

import com.mastery.java.task.dto.Employee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Sergey Tsynin
 */
@RestController
@RequestMapping("queue")
@Api(value = "async", tags = "async")
public class AsyncController {

    public AsyncController() {
        LOGGER.debug("Async producer controller was created");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncController.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * Send into queue to create new employee record.
     *
     * @param employee object.
     */
    @ApiOperation(value = "Send into queue to create new employee record", tags = "async")
    @ApiResponse(code = 400, message = "Validation error")
    @PostMapping(consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void sendToQueueEmployee(@Valid @RequestBody Employee employee) {
        LOGGER.info(" IN: sendToQueueEmployee() - [{}]", employee);
        jmsTemplate.convertAndSend("employee-queue", employee);
    }
}
