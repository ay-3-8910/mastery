package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class IdMismatchException extends RuntimeException {

    public IdMismatchException(String message) {
        super(message);
    }
}
