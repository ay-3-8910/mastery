package com.mastery.java.task.rest.excepton_handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author Sergey Tsynin
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(NotFoundMasteryException exception) {
        String message = exception.getMessage();
        LOGGER.error(message);
        return message;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationsErrors(MethodArgumentNotValidException exception) {
        LOGGER.error(exception.getMessage());
        return exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException exception) {
        LOGGER.error(exception.getMessage());
        return "Validation error.";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnknownExceptions(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        return "Wow! Happened unknown error!";
    }
}
