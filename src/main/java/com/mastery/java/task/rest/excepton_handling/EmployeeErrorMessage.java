package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class EmployeeErrorMessage {

    private String info;

    public EmployeeErrorMessage() {
    }

    public EmployeeErrorMessage(Exception exception) {
        this.info = exception.getMessage();
    }

    public EmployeeErrorMessage(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
