package com.mastery.java.task.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Sergey Tsynin
 */
@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(public * com.mastery.java.task.rest.EmployeeController.*())")
    private void allMethodsWithoutParameters() {
    }

    @Pointcut("execution(public * com.mastery.java.task.rest.EmployeeController.*(..))")
    private void allMethodsFromEmployeeController() {
    }

    @Pointcut("!allMethodsWithoutParameters() && allMethodsFromEmployeeController()")
    private void allMethodsWithParameters() {
    }

    @Around("execution(public * com.mastery.java.task.rest.EmployeeController.getAllEmployees())")
    public Object aroundGetAllEmployeesLoggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = getMethodName(joinPoint);
        loggingMessageBefore(methodName, joinPoint);

        Object result = joinPoint.proceed();

        LOGGER.info("OUT: getAllEmployees() - found {} employee(s)", ((List<?>) result).size());
        return result;
    }

    @Around("execution(public * com.mastery.java.task.rest.EmployeeController.getEmployeesCount())")
    public Object beforeGetEmployeesCountLoggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = getMethodName(joinPoint);
        loggingMessageBefore(methodName, joinPoint);

        Object result = joinPoint.proceed();

        LOGGER.info("OUT: {}() - found {} employee(s)", methodName, result);
        return result;
    }

    @Around("allMethodsWithParameters()")
    public Object aroundAllMethodsWithParametersLoggingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = getMethodName(joinPoint);
        loggingMessageBefore(methodName, joinPoint);

        Object result = joinPoint.proceed();

        LOGGER.info("OUT: {}() - {}", methodName, result);
        return result;
    }

    private void loggingMessageBefore(String methodName, ProceedingJoinPoint joinPoint) {
        LOGGER.info(" IN: {}() - {}",
                methodName,
                Arrays.toString(joinPoint.getArgs()));
    }

    private String getMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}
