package com.mastery.java.task.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sergey Tsynin
 */
@ExtendWith(MockitoExtension.class)
class EmployeeControllerUnitTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeControllerUnitTest.class);

    private static final String URI = "/employees";
    private static final String URI_ID = URI + "/{id}";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .alwaysDo(print())
                .build();
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
}