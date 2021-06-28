package by.example.controller.rest;

import by.example.model.Employee;
import by.example.model.Gender;
import by.example.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerTest.class);

    private static final String EMPLOYEE_ENDPOINT = "/employees";

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void shouldReturnEmployeesList() throws Exception {

        LOGGER.debug("shouldFindAllEmployees()");
        List<Employee> allEmployees = getFakeEmployeesList();

        given(employeeService.getAll()).willReturn(allEmployees);

        mockMvc.perform(get(EMPLOYEE_ENDPOINT)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is(allEmployees.get(0).getFirstName())))
                .andExpect(jsonPath("$[1].firstName", is(allEmployees.get(1).getFirstName())))
                .andExpect(jsonPath("$[2].firstName", is(allEmployees.get(2).getFirstName())))
        ;
    }

    @Test
    public void shouldReturnEmployeeById() throws Exception {

        LOGGER.debug("shouldReturnEmployeeById()");

        Integer id = 1;
        Optional<Employee> optionalEmployee = Optional.of(getFakeEmployee(id));

        given(employeeService.getById(id)).willReturn(optionalEmployee);

        mockMvc.perform(get(EMPLOYEE_ENDPOINT + "/" + id)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(optionalEmployee.get().getFirstName())))
        ;
    }

    @Test
    public void shouldReturnErrorWithUnknownEmployeeId() throws Exception {

        LOGGER.debug("shouldReturnEmployeeById()");

        Integer id = 99;
        given(employeeService.getById(id)).willReturn(Optional.empty());

        mockMvc.perform(get(EMPLOYEE_ENDPOINT + "/" + id)
        ).andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldCreateEmployee() throws Exception {

        LOGGER.debug("shouldCreateEmployee()");

        Employee newEmployee = getFakeEmployee(4);
        String json = objectMapper.writeValueAsString(newEmployee);
        given(employeeService.createEmployee(any())).willReturn(42);

        MockHttpServletResponse response = mockMvc.perform(post(EMPLOYEE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        assertNotNull(response);
        assertEquals(42, getIntegerValue(response));
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {

        LOGGER.debug("shouldUpdateEmployee()");

        Employee newEmployee = getFakeEmployee(1);
        String json = objectMapper.writeValueAsString(newEmployee);
        given(employeeService.updateEmployee(any())).willReturn(1);

        MockHttpServletResponse response = mockMvc.perform(put(EMPLOYEE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response);
        assertEquals(1, getIntegerValue(response));
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {

        LOGGER.debug("shouldDeleteEmployee()");

        Integer id = 1;
        given(employeeService.deleteEmployee(id)).willReturn(1);

        MockHttpServletResponse response = mockMvc.perform(delete(EMPLOYEE_ENDPOINT + "/" + id)
        ).andDo(print())
                .andExpect(status().isNoContent())
                .andReturn().getResponse();

        assertNotNull(response);
        assertEquals(1, getIntegerValue(response));
    }

    @Test
    public void shouldReturnCountOfEmployees() throws Exception {

        LOGGER.debug("shouldReturnCountOfEmployees()");

        given(employeeService.getEmployeesCount()).willReturn(7);

        MockHttpServletResponse response = mockMvc.perform(get(EMPLOYEE_ENDPOINT + "/count")
        ).andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertNotNull(response);
        assertEquals(7, getIntegerValue(response));
    }

    private Integer getIntegerValue(MockHttpServletResponse response) throws Exception {
        return objectMapper.readValue(response.getContentAsString(), Integer.class);
    }

    private Employee getFakeEmployee(Integer id) {
        Employee employee = new Employee();
        employee.setEmployeeId(id);
        employee.setFirstName("FirstName" + id);
        employee.setLastName("LastName" + id);
        employee.setDepartmentId(id);
        employee.setJobTitle("JobTitle" + id);
        employee.setGender(Gender.UNSPECIFIED);
        employee.setDateOfBirth(LocalDate.now());
        return employee;
    }

    private List<Employee> getFakeEmployeesList() {
        return Arrays.asList(
                getFakeEmployee(1),
                getFakeEmployee(2),
                getFakeEmployee(3)
        );
    }
}