package com.mastery.java.task.rest.excepton_handling;

/**
 * @author Sergey Tsynin
 */
public class EmployeeIncorrectData {

    private String info;

    public EmployeeIncorrectData() {
    }

    public EmployeeIncorrectData(Exception exception) {
        this.info = exception.getMessage();
    }

    public EmployeeIncorrectData(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
