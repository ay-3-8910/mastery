package by.example.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class EmployeeControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerTest.class);

    private static final String URI = "/employees";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    private EmployeeController employeeController;

//    private EmployeeRepository employeeRepository;


    //    public EmployeeControllerIntegrationTest(EmployeeController employeeController) {
//        this.employeeController = employeeController;
//    }
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(employeeController)
//                .setMessageConverters(new MappingJackson2HttpMessageConverter())
////                .setControllerAdvice(customExceptionHandler)
//                .alwaysDo(MockMvcResultHandlers.print())
//                .build();
//    }
//    @Test
//    public void testEmployeeRepositoryConnection() {
//        LOGGER.debug("testEmployeeRepositoryConnection()");
//        Optional<Employee> optionalEmployee = employeeRepository.findById(2);
//        assertTrue(optionalEmployee.isPresent());
//        Employee employee = optionalEmployee.get();
//        System.out.println(employee);
//    }

    @Test
    public void shouldReturnEmployeesCount() throws Exception {
        LOGGER.debug("shouldReturnEmployeesCount()");

        MockHttpServletResponse response = mockMvc.perform(get(URI + "/count")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn().getResponse();

        assertNotNull(response);
        Integer employeesCount = objectMapper.readValue(
                response.getContentAsString(),
                Integer.class);
        assertNotNull(employeesCount);
        assertEquals(6, employeesCount);

//        List<Employee> allEmployees = employeeService.findAll();
//
//        given(employeeService.getAll()).willReturn(allEmployees);
//
//        mockMvc.perform(get(EMPLOYEE_ENDPOINT)
//        ).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].firstName", is(allEmployees.get(0).getFirstName())))
//                .andExpect(jsonPath("$[1].firstName", is(allEmployees.get(1).getFirstName())))
//                .andExpect(jsonPath("$[2].firstName", is(allEmployees.get(2).getFirstName())))
        ;
    }

//    class MockMvcEmployeeService {
//
//        public List<Employee> findAll() throws Exception {
//            MockHttpServletResponse response = mockMvc.perform(get(EMPLOYEE_ENDPOINT)
//                    .accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andReturn().getResponse();
//
//            assertNotNull(response);
//
//            return objectMapper.readValue(
//                    response.getContentAsString(),
//                    new TypeReference<>() {
//                    });
//        }
//    }
}