package by.example.controller.rest;

import by.example.model.Employee;
import by.example.model.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
//@Disabled
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@EnableJpaRepositories("by.example.dao")
//@SpringBootTest
//@Transactional

@WebMvcTest(controllers = EmployeeController.class)
@ContextConfiguration(classes = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:db-schema.sql", "classpath:db-init.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EmployeeControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerTest.class);

    private static final String URI = "/employees";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvcEmployeeService employeeService = new MockMvcEmployeeService();

    @Autowired
    private MockMvc mockMvc;

    private EmployeeController employeeController;

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
    public void shouldReturnEmployeesCount() throws Exception {
        LOGGER.debug("shouldReturnEmployeesCount()");

        Integer employeesCount = employeeService.count();

        assertNotNull(employeesCount);
        assertEquals(3, employeesCount);
    }

    private class MockMvcEmployeeService {

        public Optional<Employee> findById(Integer id) throws Exception {
            MockHttpServletResponse servletResponse = getHttpServletResponse(URI + "/" + id);
            assertNotNull(servletResponse);
            return getOptionalEmployee(servletResponse);
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