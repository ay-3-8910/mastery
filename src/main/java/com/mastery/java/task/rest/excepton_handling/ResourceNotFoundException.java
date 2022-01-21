package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
