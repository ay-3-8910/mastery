package com.mastery.java.task.rest.excepton_handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Sergey Tsynin
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EmployeeErrorMessage handleNotFoundException(NotFoundMasteryException exception) {
        LOGGER.error(exception.getMessage()); //??
        return new EmployeeErrorMessage(exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EmployeeErrorMessage handleValidationsErrors(MethodArgumentNotValidException exception) {
        LOGGER.error(exception.getMessage());
        return new EmployeeErrorMessage(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EmployeeErrorMessage handleUnknownExceptions(Exception exception) {
        LOGGER.error(exception.getMessage());
        return new EmployeeErrorMessage("Ups! happened unknown error.");
    }
}
