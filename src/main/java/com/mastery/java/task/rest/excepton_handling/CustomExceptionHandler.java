package com.mastery.java.task.rest.excepton_handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Sergey Tsynin
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorMessage> handleNotFoundException(NotFoundEmployeeException exception) {
        LOGGER.error("Handled not found exception");
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(
                new EmployeeErrorMessage(exception),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorMessage> handleValidationsErrors(MethodArgumentNotValidException exception) {
        LOGGER.error("Handled validation error");
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(
                new EmployeeErrorMessage(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorMessage> handleUnknownExceptions(Exception exception) {
        LOGGER.error("Handled unknown error");
        return new ResponseEntity<>(
                new EmployeeErrorMessage(exception),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
