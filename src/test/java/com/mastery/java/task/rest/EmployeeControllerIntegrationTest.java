package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.rest.excepton_handling.EmployeeErrorMessage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EmployeeControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerIntegrationTest.class);

    private static final String URI = "/employees";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvcEmployeeService employeeService = new MockMvcEmployeeService();

    @Autowired
    private MockMvc mockMvc;

    private EmployeeController employeeController;

    @Test
    public void shouldReturnEmployeesList() throws Exception {
        LOGGER.debug("shouldReturnEmployeesList()");

        List<Employee> employees = employeeService.findAll();

        assertNotNull(employees);
        assertEquals(3, employees.size());
    }

    @Test
    public void shouldReturnEmployeeById() throws Exception {
        LOGGER.debug("shouldReturnEmployeeById()");
        Integer id = 2;

        Optional<Employee> optionalEmployee = employeeService.findById(id);
        assertTrue(optionalEmployee.isPresent());

        Employee employee = optionalEmployee.get();
        assertEquals(2, employee.getEmployeeId());
        assertEquals("Rudolph", employee.getFirstName());
        assertEquals("the Deer", employee.getLastName());
        assertEquals(2, employee.getDepartmentId());
        assertEquals("bottles washer", employee.getJobTitle());
        assertEquals(Gender.UNSPECIFIED, employee.getGender());
        assertEquals(LocalDate.of(2018, 8, 16), employee.getDateOfBirth());
    }

    @Test
    public void shouldReturnNotFoundWithTryToFindUnknownId() throws Exception {
        LOGGER.debug("shouldReturnNotFoundWithTryToFindUnknownId()");
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.get(URI + "/999")
        ).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);

        EmployeeErrorMessage errorMessage = objectMapper.readValue(
                response.getContentAsString(),
                EmployeeErrorMessage.class);
        assertNotNull(errorMessage);
        assertEquals("Employee id:999 was not found in database", errorMessage.getInfo());
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        LOGGER.debug("shouldCreateEmployee()");
        Employee newEmployee = getFakeEmployee(128);
        Integer newEmployeeId = employeeService.create(newEmployee);
        assertNotNull(newEmployeeId);
        assertEquals(4, newEmployeeId);
        assertEquals(4, employeeService.count());
    }

    @Test
    public void shouldReturnUnprocessableEntityIfCreateTooYoungEmployee() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityIfCreateEmployeeWithNullFirstName()");
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setDateOfBirth(LocalDate.now());

        EmployeeErrorMessage errorMessage = getErrorMessage(employeeService.tryToCreateEmployee(newEmployee));

        assertNotNull(errorMessage);
        assertEquals("The employee must be over 18 years old", errorMessage.getInfo());
    }

    @Test
    public void shouldReturnUnprocessableEntityIfCreateEmployeeWithNullFirstName() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityIfCreateEmployeeWithNullFirstName()");
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setFirstName(null);

        EmployeeErrorMessage errorMessage = getErrorMessage(employeeService.tryToCreateEmployee(newEmployee));

        assertNotNull(errorMessage);
        assertEquals("Employee firstname cannot be empty", errorMessage.getInfo());
    }

    @Test
    public void shouldReturnUnprocessableEntityIfCreateEmployeeWithNullLastName() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityIfCreateEmployeeWithNullLastName()");
        Employee newEmployee = getFakeEmployee(128);
        newEmployee.setLastName(null);
        employeeService.tryToCreateEmployee(newEmployee);
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        LOGGER.debug("shouldUpdateEmployee()");
        Integer id = 2;
        String newJobTitle = "head of bottles washing";

        Optional<Employee> optionalEmployee = employeeService.findById(id);
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();
        employee.setJobTitle(newJobTitle);
        employeeService.update(employee, status().isOk());

        assertEquals(3, employeeService.count());
    }

    @Test
    public void shouldReturnNotFoundIfUpdateEmployeeWithUnknownId() throws Exception {
        LOGGER.debug("shouldReturnNotFoundIfUpdateEmployeeWithUnknownId()");
        Integer id = 2;
        Integer newId = 99;

        Optional<Employee> optionalEmployee = employeeService.findById(id);
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();
        employee.setEmployeeId(newId);
        employeeService.update(employee, status().isNotFound());

        assertEquals(3, employeeService.count());
    }

    @Test
    public void shouldReturnUnprocessableEntityIfUpdateEmployeeWithNullFirstName() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityIfUpdateEmployeeWithNullFirstName()");
        Optional<Employee> optionalEmployee = employeeService.findById(2);
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();

        employee.setFirstName(null);
        employeeService.update(employee, status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnUnprocessableEntityIfUpdateEmployeeWithNullLastName() throws Exception {
        LOGGER.debug("shouldReturnUnprocessableEntityIfUpdateEmployeeWithNullLastName()");
        Optional<Employee> optionalEmployee = employeeService.findById(2);
        assertTrue(optionalEmployee.isPresent());
        Employee employee = optionalEmployee.get();

        employee.setLastName(null);
        employeeService.update(employee, status().isUnprocessableEntity());
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        LOGGER.debug("shouldDeleteEmployee()");
        employeeService.deleteById(2);
        assertEquals(2, employeeService.count());
    }

    @Test
    public void shouldReturnNotFoundForDeleteEmployeeWithWrongId() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders.delete(URI + "/999")
        ).andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        assertNotNull(response);

        EmployeeErrorMessage errorMessage = objectMapper.readValue(
                response.getContentAsString(),
                EmployeeErrorMessage.class);
        assertNotNull(errorMessage);
        assertEquals("Employee id:999 was not found in database", errorMessage.getInfo());

        assertEquals(3, employeeService.count());
    }

    @Test
    public void shouldReturnEmployeesCount() throws Exception {
        LOGGER.debug("shouldReturnEmployeesCount()");

        Integer employeesCount = employeeService.count();

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

    private EmployeeErrorMessage getErrorMessage(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(
                response.getContentAsString(),
                EmployeeErrorMessage.class);
    }

    private class MockMvcEmployeeService {

        public List<Employee> findAll() throws Exception {
            MockHttpServletResponse servletResponse = getHttpServletResponse(URI + "/");
            assertNotNull(servletResponse);
            return objectMapper.readValue(
                    servletResponse.getContentAsString(),
                    new TypeReference<>() {
                    });
        }

        public Optional<Employee> findById(Integer id) throws Exception {
            MockHttpServletResponse servletResponse = getHttpServletResponse(URI + "/" + id);
            assertNotNull(servletResponse);
            return getOptionalEmployee(servletResponse);
        }

        public Integer create(Employee employee) throws Exception {
            String json = objectMapper.writeValueAsString(employee);
            MockHttpServletResponse servletResponse = getHttpServletResponseForPost(json);
            assertNotNull(servletResponse);
            return getOptionalInteger(servletResponse);
        }

        public MockHttpServletResponse tryToCreateEmployee(Employee employee) throws Exception {
            String json = objectMapper.writeValueAsString(employee);
            MockHttpServletResponse servletResponse = getHttpServletResponseForBadPost(
                    json, status().isUnprocessableEntity());
            assertNotNull(servletResponse);
            return servletResponse;
        }

        public void update(Employee employee, ResultMatcher expectedStatus) throws Exception {
            String json = objectMapper.writeValueAsString(employee);
            MockHttpServletResponse servletResponse = getHttpServletResponseWithEmptyBody(json, expectedStatus);
            assertNotNull(servletResponse);
        }

        public void deleteById(Integer id) throws Exception {
            MockHttpServletResponse servletResponse = mockMvc.perform(
                    MockMvcRequestBuilders.delete(URI + "/" + id))
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andReturn().getResponse();
            assertNotNull(servletResponse);
        }

        public Integer count() throws Exception {
            MockHttpServletResponse servletResponse = getHttpServletResponse(URI + "/count");
            assertNotNull(servletResponse);
            return getOptionalInteger(servletResponse);
        }

        private MockHttpServletResponse getHttpServletResponse(String urlTemplate) throws Exception {
            return mockMvc.perform(get(urlTemplate)
            ).andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andReturn().getResponse();
        }

        private MockHttpServletResponse getHttpServletResponseForPost(String json) throws Exception {
            return mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json"))
                    .andReturn().getResponse();
        }

        private MockHttpServletResponse getHttpServletResponseForBadPost(String json, ResultMatcher expectedStatus) throws Exception {
            return mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
                    .andExpect(expectedStatus)
                    .andReturn().getResponse();
        }

        private MockHttpServletResponse getHttpServletResponseWithEmptyBody(String json, ResultMatcher expectedStatus) throws Exception {
            return mockMvc.perform(put(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON)
            ).andDo(print())
                    .andExpect(expectedStatus)
                    .andExpect(jsonPath("$").doesNotExist())
                    .andReturn().getResponse();
        }

        private Optional<Employee> getOptionalEmployee(MockHttpServletResponse servletResponse) throws Exception {
            return Optional.of(objectMapper.readValue(
                    servletResponse.getContentAsString(),
                    Employee.class));
        }

        private Integer getOptionalInteger(MockHttpServletResponse servletResponse) throws Exception {
            return objectMapper.readValue(
                    servletResponse.getContentAsString(),
                    Integer.class);
        }
    }
}