package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class NotFoundMasteryException extends RuntimeException {

    public NotFoundMasteryException(Integer employeeId) {
        super("Employee id: " + employeeId + " was not found in database");
    }
}
