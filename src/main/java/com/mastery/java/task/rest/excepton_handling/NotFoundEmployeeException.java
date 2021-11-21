package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class NotFoundEmployeeException extends RuntimeException {

    public NotFoundEmployeeException(String message) {
        super(message);
    }
}
