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

INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, DEPARTMENT_ID, JOB_TITLE, GENDER, DATE_OF_BIRTH)
    VALUES ('Vasily', 'Pupkin', '1', 'director', 'MALE', '2017-04-28');
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, DEPARTMENT_ID, JOB_TITLE, GENDER, DATE_OF_BIRTH)
    VALUES ('Rudolph', 'the Deer', '2', 'bottles washer','UNSPECIFIED', '2000-08-16');
INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, DEPARTMENT_ID, JOB_TITLE, GENDER, DATE_OF_BIRTH)
    VALUES ('Zaphod', 'Beeblebrox', '42', 'president','MALE', '2011-11-11');