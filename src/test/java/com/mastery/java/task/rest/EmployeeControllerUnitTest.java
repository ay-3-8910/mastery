package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerUnitTest.class);

    private static final String URI = "/employees";
    private static final String URI_ID = URI + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void shouldReturnEmployeesList() throws Exception {
        LOGGER.debug("shouldReturnEmployeesList()");

        // given
        List<Employee> employees = new ArrayList<>();
        employees.add(getFakeEmployee(1));
        employees.add(getFakeEmployee(2));
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON)

                ) // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(2, extractEmployeeList(servletResponse).size());
        assertEquals(employees, extractEmployeeList(servletResponse));
        verify(employeeService).getAllEmployees();
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        LOGGER.debug("shouldReturnEmployeeById()");

        // given
        Integer employeeToInteractionId = 42;
        Employee employee = getFakeEmployee(employeeToInteractionId);
        when(employeeService.getEmployeeById(employeeToInteractionId)).thenReturn(employee);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI_ID, employeeToInteractionId)
                        .accept(MediaType.APPLICATION_JSON)

                ) // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(employee, extractEmployee(servletResponse));
        verify(employeeService).getEmployeeById(employeeToInteractionId);
    }

    @Test
    void shouldReturnEmployeeByName() throws Exception {
        LOGGER.debug("shouldReturnEmployeeByName()");

        // given
        String firstName = "FirstName7";
        String lastName = "LastName7";
        List<Employee> employeesList = Collections.singletonList(getFakeEmployee(7));
        when(employeeService.getEmployeesByName(firstName, lastName)).thenReturn(employeesList);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI + "/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .accept(MediaType.APPLICATION_JSON)

                ) // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(employeesList, extractEmployeeList(servletResponse));
        verify(employeeService).getEmployeesByName(firstName, lastName);
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        LOGGER.debug("shouldCreateEmployee()");

        // given
        Integer employeeToInteractionId = 128;
        Employee newEmployee = getFakeEmployee(employeeToInteractionId);
        Employee returnedEmployee = getFakeEmployee(employeeToInteractionId);
        newEmployee.setEmployeeId(null);
        String json = objectMapper.writeValueAsString(newEmployee);
        when(employeeService.createEmployee(newEmployee)).thenReturn(returnedEmployee);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(post(URI, newEmployee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)

                ) // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(employeeToInteractionId, extractEmployee(servletResponse).getEmployeeId());
        verify(employeeService).createEmployee(newEmployee);
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        LOGGER.debug("shouldUpdateEmployee()");

        // given
        Integer employeeToInteractionId = 256;
        Employee newEmployee = getFakeEmployee(employeeToInteractionId);
        String json = objectMapper.writeValueAsString(newEmployee);
        when(employeeService.updateEmployee(newEmployee)).thenReturn(newEmployee);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(put(URI_ID, 1, newEmployee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON)

                ) // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(newEmployee, extractEmployee(servletResponse));
        verify(employeeService).updateEmployee(newEmployee);
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        LOGGER.info("shouldDeleteEmployee()");

        // given
        Integer employeeToInteractionId = 1;

        // when
        mockMvc.perform(delete(URI_ID, employeeToInteractionId)

                ) // then
                .andExpect(status().isNoContent());
        verify(employeeService).deleteEmployee(employeeToInteractionId);
    }

    @Test
    void shouldReturnEmployeesCount() throws Exception {
        LOGGER.info("shouldReturnEmployeesCount()");

        // given
        Integer expectedCount = 42;
        when(employeeService.getEmployeesCount()).thenReturn(expectedCount);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI + "/count")
                        .accept(MediaType.APPLICATION_JSON)

                ) // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(servletResponse);
        assertEquals(expectedCount, extractInteger(servletResponse));
        verify(employeeService).getEmployeesCount();
    }

    private Integer extractInteger(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                Integer.class);
    }

    private Employee extractEmployee(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                Employee.class);
    }

    private List<Employee> extractEmployeeList(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                new TypeReference<>() {
                });
    }

    private Employee getFakeEmployee(Integer id) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName("FirstName" + id);
        employee.setLastName("LastName" + id);
        employee.setDepartmentId(id);
        employee.setJobTitle("JobTitle" + id);
        employee.setGender(Gender.UNSPECIFIED);
        employee.setDateOfBirth(LocalDate.now().minusYears(18));
        return employee;
    }
}