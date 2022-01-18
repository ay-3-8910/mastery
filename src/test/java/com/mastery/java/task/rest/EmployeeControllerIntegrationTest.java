package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = "com.mastery.java.task")
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerIntegrationTest.class);

    private static final String URI = "/employees";
    private static final String URI_ID = URI + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvcEmployeeService employeeService = new MockMvcEmployeeService();

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void shouldReturnEmployeesList() throws Exception {
        LOGGER.debug("shouldReturnEmployeesList()");

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertNotNull(servletResponse);
        assertEquals(3, extractEmployeeList(servletResponse).size());
    }

    @Test
    @Order(2)
    public void shouldReturnEmployeeById() throws Exception {
        LOGGER.debug("shouldReturnEmployeeById()");

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI_ID, 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        Employee employee = extractEmployee(servletResponse);

        assertEquals(2, employee.getEmployeeId());
        assertEquals("Rudolph", employee.getFirstName());
        assertEquals("the Deer", employee.getLastName());
        assertEquals(2, employee.getDepartmentId());
        assertEquals("bottles washer", employee.getJobTitle());
        assertEquals(Gender.UNSPECIFIED, employee.getGender());
        assertEquals(LocalDate.of(2000, 8, 16), employee.getDateOfBirth());
    }

    @Test
    @Order(3)
    public void shouldReturn404WithTryToFindUnknownId() throws Exception {
        LOGGER.debug("shouldReturn404WithTryToFindUnknownId()");

        // when
        mockMvc.perform(get(URI_ID, 999)
                        .accept(MediaType.ALL)
                ).andDo(print())

                // then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Employee id: 999 was not found in database"));
    }

    @Test
    @Order(4)
    public void shouldReturn400InCaseZeroIdWithGetMethod() throws Exception {
        LOGGER.debug("shouldReturn400InCaseZeroIdWithGetMethod()");

        // when
        mockMvc.perform(get(URI_ID, 0)
                        .accept(MediaType.ALL)
                ).andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Validation error."));
    }

    @Test
    @Order(5)
    public void shouldCreateEmployee() throws Exception {
        LOGGER.debug("shouldCreateEmployee()");

        // given
        Integer employeeToInteractionId = 4;
        Employee newEmployee = getFakeEmployee(employeeToInteractionId);
        newEmployee.setEmployeeId(null);
        String json = objectMapper.writeValueAsString(newEmployee);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(post(URI, newEmployee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(employeeToInteractionId, extractEmployee(servletResponse).getEmployeeId());
        assertEquals(4, employeeService.count());
    }

    @Test
    @Order(6)
    public void shouldReturn400IfCreateTooYoungEmployee() throws Exception {
        LOGGER.debug("shouldReturn400IfCreateTooYoungEmployee()");

        // given
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now());
        String json = objectMapper.writeValueAsString(newEmployee);

        // when
        mockMvc.perform(post(URI, newEmployee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The employee must be over 18 years old"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(7)
    public void shouldReturn400IfCreateEmployeeWithNullFirstName() throws Exception {
        LOGGER.debug("shouldReturn400IfCreateEmployeeWithNullFirstName()");

        // given
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setFirstName(null);
        String json = objectMapper.writeValueAsString(newEmployee);

        // when
        mockMvc.perform(post(URI, newEmployee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Employee firstname cannot be empty"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(8)
    public void shouldReturn400IfCreateEmployeeWithNullLastName() throws Exception {
        LOGGER.debug("shouldReturn400IfCreateEmployeeWithNullLastName()");

        // given
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setLastName(null);
        String json = objectMapper.writeValueAsString(newEmployee);

        // when
        mockMvc.perform(post(URI, newEmployee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Employee lastname cannot be empty"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(9)
    public void shouldUpdateEmployee() throws Exception {
        LOGGER.debug("shouldUpdateEmployee()");

        // given
        Integer id = 2;
        Employee employee = getFakeEmployee(id);
        String json = objectMapper.writeValueAsString(employee);

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(put(URI_ID, id, employee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        Employee returnedEmployee = extractEmployee(servletResponse);
        assertEquals(employee, returnedEmployee);
        assertEquals(employee, employeeService.findById(id));
        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(10)
    public void shouldReturn404IfUpdateEmployeeWithUnknownId() throws Exception {
        LOGGER.debug("shouldReturn404IfUpdateEmployeeWithUnknownId()");

        // given
        Integer id = 99;
        Employee employee = getFakeEmployee(id);
        String json = objectMapper.writeValueAsString(employee);

        // when
        mockMvc.perform(put(URI_ID, id, employee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Employee id: 99 was not found in database"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(11)
    public void shouldReturn400IfUpdateEmployeeWithNullFirstName() throws Exception {
        LOGGER.debug("shouldReturn400IfUpdateEmployeeWithNullFirstName()");

        // given
        Integer id = 2;
        Employee employee = getFakeEmployee(id);
        employee.setFirstName(null);
        String json = objectMapper.writeValueAsString(employee);

        // when
        mockMvc.perform(put(URI_ID, id, employee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Employee firstname cannot be empty"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(12)
    public void shouldReturn400IfUpdateEmployeeWithNullLastName() throws Exception {
        LOGGER.debug("shouldReturn400IfUpdateEmployeeWithNullLastName()");

        // given
        Integer id = 2;
        Employee employee = getFakeEmployee(id);
        employee.setLastName(null);
        String json = objectMapper.writeValueAsString(employee);

        // when
        mockMvc.perform(put(URI_ID, id, employee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Employee lastname cannot be empty"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(13)
    public void shouldReturn400IfUpdateEmployeeWithLowAge() throws Exception {
        LOGGER.debug("shouldReturn400IfUpdateEmployeeWithLowAge()");
        Integer id = 2;
        Employee employee = getFakeEmployee(id);
        employee.setDateOfBirth(LocalDate.now());
        String json = objectMapper.writeValueAsString(employee);

        // when
        mockMvc.perform(put(URI_ID, id, employee)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("The employee must be over 18 years old"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(14)
    public void shouldDeleteEmployee() throws Exception {
        LOGGER.debug("shouldDeleteEmployee()");

        // when
        mockMvc.perform(delete(URI_ID, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        assertEquals(2, employeeService.count());
    }

    @Test
    @Order(15)
    public void shouldReturn404ForDeleteEmployeeWithWrongId() throws Exception {
        LOGGER.debug("shouldReturn404ForDeleteEmployeeWithWrongId");

        // when
        mockMvc.perform(delete(URI_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())

                // then
                .andExpect(status().isNotFound())
                .andExpect(content().string("Employee id: 99 was not found in database"));

        assertEquals(3, employeeService.count());
    }

    @Test
    @Order(16)
    public void shouldReturnEmployeesCount() throws Exception {
        LOGGER.debug("shouldReturnEmployeesCount()");

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI + "/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())

                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        Integer employeesCount = extractInteger(servletResponse);
        assertNotNull(employeesCount);
        assertEquals(3, employeesCount);
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

    private Integer extractInteger(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                Integer.class);
    }

    private class MockMvcEmployeeService {

        public Employee findById(Integer id) throws Exception {
            return extractEmployee(mockMvc.perform(get(URI_ID, id)).andReturn().getResponse());
        }

        public Integer count() throws Exception {
            return extractInteger(mockMvc.perform(get(URI + "/count")).andReturn().getResponse());
        }
    }
}