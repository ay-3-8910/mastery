package com.mastery.java.task.rest.excepton_handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Sergey Tsynin
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleNotFoundException(NotFoundEmployeeException exception) {
        return new ResponseEntity<>(
                new EmployeeIncorrectData(exception),
                HttpStatus.NOT_FOUND);
    }
}
