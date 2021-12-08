package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class NotFoundMasteryException extends RuntimeException {

    public NotFoundMasteryException(String message) {
        super(message);
    }
}
