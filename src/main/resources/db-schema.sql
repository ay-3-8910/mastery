DROP TABLE IF EXISTS EMPLOYEE;

CREATE TABLE EMPLOYEE
(
    EMPLOYEE_ID SERIAL PRIMARY KEY,
    FIRST_NAME VARCHAR(128) NOT NULL,
    LAST_NAME VARCHAR(128) NOT NULL,
    DEPARTMENT_ID INT,
    JOB_TITLE VARCHAR(128),
    GENDER VARCHAR(128),
    DATE_OF_BIRTH DATE
);