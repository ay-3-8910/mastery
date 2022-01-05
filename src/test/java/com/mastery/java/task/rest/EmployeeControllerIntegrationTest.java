package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.rest.excepton_handling.CustomExceptionHandler;
import com.mastery.java.task.rest.excepton_handling.EmployeeErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestConfig.class)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestPropertySource({"classpath:sql.properties", "classpath:application.properties"})
public class EmployeeControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerIntegrationTest.class);

    private static final String URI = "/employees";
    private static final String URI_ID = URI + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvcEmployeeService employeeService = new MockMvcEmployeeService();

    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setControllerAdvice(new CustomExceptionHandler())
                .alwaysDo(print())
                .build();
    }

    @Test
    public void shouldReturnEmployeesList() throws Exception {
        LOGGER.debug("shouldReturnEmployeesList()");

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI)
                .accept(MediaType.APPLICATION_JSON)

        ) // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertNotNull(servletResponse);
        assertEquals(3, extractEmployeeList(servletResponse).size());
    }

    @Test
    public void shouldReturnEmployeeById() throws Exception {
        LOGGER.debug("shouldReturnEmployeeById()");

        // when
        MockHttpServletResponse servletResponse = mockMvc.perform(get(URI_ID, 2)
                .accept(MediaType.APPLICATION_JSON)

        ) // then
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
    public void shouldReturn404WithTryToFindUnknownId() throws Exception {
        LOGGER.debug("shouldReturn404WithTryToFindUnknownId()");
        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.read(999, status().isNotFound()));

        assertNotNull(errorMessage);
        assertEquals("Employee id: 999 was not found in database", errorMessage.getInfo());
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        LOGGER.debug("shouldCreateEmployee()");
        Employee newEmployee = getFakeEmployee(128);

        Employee returnedEmployee = extractEmployee(employeeService.create(
                newEmployee,
                status().isCreated()));

        newEmployee.setEmployeeId(4);
        assertEquals(4, employeeService.count());
        assertEquals(newEmployee, returnedEmployee);
        assertEquals(newEmployee, employeeService.findById(returnedEmployee.getEmployeeId()));
    }

    @Test
    public void shouldReturn400IfCreateTooYoungEmployee() throws Exception {
        LOGGER.debug("shouldReturn400IfCreateTooYoungEmployee()");
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now());

        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.create(
                newEmployee,
                status().isBadRequest()));

        assertNotNull(errorMessage);
        assertEquals("The employee must be over 18 years old", errorMessage.getInfo());
    }

    @Test
    public void shouldReturn400IfCreateEmployeeWithNullFirstName() throws Exception {
        LOGGER.debug("shouldReturn400IfCreateEmployeeWithNullFirstName()");
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setFirstName(null);

        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.create(
                newEmployee,
                status().isBadRequest()));

        assertNotNull(errorMessage);
        assertEquals("Employee firstname cannot be empty", errorMessage.getInfo());
    }

    @Test
    public void shouldReturn400IfCreateEmployeeWithNullLastName() throws Exception {
        LOGGER.debug("shouldReturn400IfCreateEmployeeWithNullLastName()");
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setLastName(null);
        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.create(
                newEmployee,
                status().isBadRequest()));

        assertNotNull(errorMessage);
        assertEquals("Employee lastname cannot be empty", errorMessage.getInfo());
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        LOGGER.debug("shouldUpdateEmployee()");
        Integer id = 2;
        Employee employee = getFakeEmployee(id);

        extractEmployee(employeeService.update(id, employee, status().isOk()));

        assertEquals(3, employeeService.count());
        assertEquals(employee, employeeService.findById(id));
    }

    @Test
    public void shouldReturn404IfUpdateEmployeeWithUnknownId() throws Exception {
        LOGGER.debug("shouldReturn404IfUpdateEmployeeWithUnknownId()");
        Integer id = 99;
        Employee employee = getFakeEmployee(id);

        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.update(
                id,
                employee,
                status().isNotFound()));

        assertEquals(3, employeeService.count());
        assertEquals("Employee was not found in database", errorMessage.getInfo());
    }

    @Test
    public void shouldReturn400IfUpdateEmployeeWithNullFirstName() throws Exception {
        LOGGER.debug("shouldReturn400IfUpdateEmployeeWithNullFirstName()");
        Integer id = 2;
        Employee employee = employeeService.findById(id);

        employee.setFirstName(null);
        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.update(
                id,
                employee,
                status().isBadRequest()));
        assertEquals(3, employeeService.count());

        assertNotNull(errorMessage);
        assertEquals("Employee firstname cannot be empty", errorMessage.getInfo());
    }

    @Test
    public void shouldReturn400IfUpdateEmployeeWithNullLastName() throws Exception {
        LOGGER.debug("shouldReturn400IfUpdateEmployeeWithNullLastName()");
        Integer id = 2;
        Employee employee = employeeService.findById(id);

        employee.setLastName(null);
        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.update(
                id,
                employee,
                status().isBadRequest()));
        assertEquals(3, employeeService.count());

        assertNotNull(errorMessage);
        assertEquals("Employee lastname cannot be empty", errorMessage.getInfo());
    }

    @Test
    public void shouldReturn400IfUpdateEmployeeWithLowAge() throws Exception {
        LOGGER.debug("shouldReturn400IfUpdateEmployeeWithLowAge()");
        Integer id = 2;
        Employee employee = employeeService.findById(id);

        employee.setDateOfBirth(LocalDate.now());
        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.update(
                id,
                employee,
                status().isBadRequest()));
        assertEquals(3, employeeService.count());

        assertNotNull(errorMessage);
        assertEquals("The employee must be over 18 years old", errorMessage.getInfo());
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        LOGGER.debug("shouldDeleteEmployee()");

        // when
        mockMvc.perform(delete(URI_ID, 2)

        ) // then
                .andExpect(status().isNoContent());
        assertEquals(2, employeeService.count());
    }

    @Test
    public void shouldReturn404ForDeleteEmployeeWithWrongId() throws Exception {
        LOGGER.debug("shouldReturn404ForDeleteEmployeeWithWrongId");
        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.delete(
                999,
                status().isNotFound(),
                content().contentType("application/json")));

        assertNotNull(errorMessage);
        assertEquals("Employee id: 999 was not found in database", errorMessage.getInfo());

        assertEquals(3, employeeService.count());
    }

    @Test
    public void shouldReturnEmployeesCount() throws Exception {
        LOGGER.debug("shouldReturnEmployeesCount()");

        Integer employeesCount = employeeService.count();

        assertNotNull(employeesCount);
        assertEquals(3, employeesCount);
    }

    @Test
    public void shouldReturn400InCaseInvalidIdWithGetMethod() throws Exception {
        LOGGER.debug("shouldReturn400InCaseInvalidIdWithGetMethod()");

        EmployeeErrorMessage errorMessage = extractErrorMessage(employeeService.read(
                0,
                status().isBadRequest()));

        assertNotNull(errorMessage);
        assertEquals("Validation error.", errorMessage.getInfo());
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

    private Integer extractInteger(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                Integer.class);
    }

    private List<Employee> extractEmployeeList(MockHttpServletResponse servletResponse) throws Exception {
        return objectMapper.readValue(
                servletResponse.getContentAsString(),
                new TypeReference<>() {
                });
    }

    private EmployeeErrorMessage extractErrorMessage(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(
                response.getContentAsString(),
                EmployeeErrorMessage.class);
    }

    private class MockMvcEmployeeService {

        public Employee findById(Integer id) throws Exception {
            return extractEmployee(read(id, status().isOk()));
        }

        public MockHttpServletResponse read(Integer id,
                                            ResultMatcher expectedStatus) throws Exception {
            MockHttpServletResponse servletResponse = executeGetMethod(URI + "/" + id, expectedStatus);
            assertNotNull(servletResponse);
            return servletResponse;
        }

        public MockHttpServletResponse create(Employee employee,
                                              ResultMatcher expectedStatus) throws Exception {
            String json = objectMapper.writeValueAsString(employee);
            MockHttpServletResponse servletResponse = executePostMethod(
                    json,
                    expectedStatus,
                    content().contentType("application/json"));
            assertNotNull(servletResponse);
            return servletResponse;
        }

        public MockHttpServletResponse update(Integer id,
                                              Employee employee,
                                              ResultMatcher expectedStatus) throws Exception {
            String json = objectMapper.writeValueAsString(employee);
            MockHttpServletResponse servletResponse = executePutMethod(
                    id,
                    json,
                    expectedStatus);
            assertNotNull(servletResponse);
            return servletResponse;
        }

        public MockHttpServletResponse delete(Integer id,
                                              ResultMatcher expectedStatus,
                                              ResultMatcher expectedContent) throws Exception {
            MockHttpServletResponse servletResponse = executeDeleteMethod(
                    id,
                    expectedStatus,
                    expectedContent);
            assertNotNull(servletResponse);
            return servletResponse;
        }

        public Integer count() throws Exception {
            MockHttpServletResponse servletResponse = executeGetMethod(URI + "/count", status().isOk());
            assertNotNull(servletResponse);
            return extractInteger(servletResponse);
        }

        private MockHttpServletResponse executeGetMethod(String urlTemplate, ResultMatcher expectedStatus) throws Exception {
            return mockMvc.perform(get(urlTemplate)
            ).andDo(print())
                    .andExpect(expectedStatus)
                    .andExpect(content().contentType("application/json"))
                    .andReturn().getResponse();
        }

        private MockHttpServletResponse executePostMethod(String json,
                                                          ResultMatcher expectedStatus,
                                                          ResultMatcher expectedContent) throws Exception {
            return mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
                    .andExpect(expectedStatus)
                    .andExpect(expectedContent)
                    .andReturn().getResponse();
        }

        private MockHttpServletResponse executePutMethod(Integer id,
                                                         String json,
                                                         ResultMatcher expectedStatus) throws Exception {
            return mockMvc.perform(put(URI + "/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
                    .andExpect(expectedStatus)
                    .andReturn().getResponse();
        }

        private MockHttpServletResponse executeDeleteMethod(Integer id,
                                                            ResultMatcher expectedStatus,
                                                            ResultMatcher expectedContent) throws Exception {
            return mockMvc.perform(
                    MockMvcRequestBuilders.delete(URI + "/" + id)
            ).andDo(print())
                    .andExpect(expectedStatus)
                    .andExpect(expectedContent)
                    .andReturn().getResponse();
        }
    }
}