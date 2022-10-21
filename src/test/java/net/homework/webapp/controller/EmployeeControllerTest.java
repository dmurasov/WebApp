package net.homework.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.homework.webapp.dto.EmployeeDto;
import net.homework.webapp.exception.EmployeeNotFoundException;
import net.homework.webapp.model.Department;
import net.homework.webapp.model.Employee;
import net.homework.webapp.service.DepartmentService;
import net.homework.webapp.service.EmployeeService;
import net.homework.webapp.service.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import net.homework.webapp.service.mapper.EmployeeMapper.*;

@WebMvcTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private MockMvc mockMvc;

    public static final Department VALID_DEPARTMENT = Department.builder()
            .id(1L)
            .departmentName("IT")
            .location("Chisinau")
            .build();

    public static final EmployeeDto VALID_EMPLOYEE_DTO = EmployeeDto.builder()
                    .id(4L)
                    .firstName("Christian")
                    .lastName("Cristopher")
                    .email("Chris@gmail.com")
                    .phoneNumber("590.423.4567")
                    .departmentId(1L)
                    .salary(5000.0)
                    .build();

    public static final Employee VALID_EMPLOYEE = Employee.builder()
            .id(4L)
            .firstName("Christian")
            .lastName("Cristopher")
            .email("Chris@gmail.com")
            .phoneNumber("590.423.4567")
            .department(VALID_DEPARTMENT)
            .salary(5000.0)
            .build();

    public static final Employee VALID_EMPLOYEE_ADD = Employee.builder()
            .id(4L)
            .firstName("Christian")
            .lastName("Cristopher")
            .email("Chris@gmail.com")
            .phoneNumber("590.423.4567")
            .salary(5000.0)
            .build();

    public static final EmployeeDto INVALID_EMPLOYEE_DTO = EmployeeDto.builder()
            .id(10L)
            .firstName("f")
            .lastName("")
            .email("A")
            .phoneNumber("590.423.4567")
            .departmentId(1L)
            .salary(0.1)
            .build();

    public static final List<Employee> EMPLOYEE_LIST = Arrays.asList(
            Employee.builder()
                    .id(1L)
                    .firstName("Pat")
                    .lastName("Fay")
                    .email("Fpay@Gmail.com")
                    .phoneNumber("603.123.6666")
                    .department(VALID_DEPARTMENT)
                    .salary(6000.0)
                    .build(),
            Employee.builder()
                    .id(2L)
                    .firstName("Diana")
                    .lastName("Lorentz")
                    .email("Lorentz@Gmail.com")
                    .phoneNumber("590.423.5567")
                    .department(VALID_DEPARTMENT)
                    .salary(4200.0)
                    .build()
    );

    public static final List<EmployeeDto> EMPLOYEE_LIST_DTO = Arrays.asList(
            EmployeeDto.builder()
                    .id(1L)
                    .firstName("Pat")
                    .lastName("Fay")
                    .email("Fpay@Gmail.com")
                    .phoneNumber("603.123.6666")
                    .departmentId(1L)
                    .salary(6000.0)
                    .build(),
            EmployeeDto.builder()
                    .id(2L)
                    .firstName("Diana")
                    .lastName("Lorentz")
                    .email("Lorentz@Gmail.com")
                    .phoneNumber("590.423.5567")
                    .departmentId(1L)
                    .salary(4200.0)
                    .build()
    );

    @Test
    void getById_ShouldReturnNotFoundIfIdIsInvalid() throws Exception {
        when(employeeService.getById(3L)).thenThrow(new EmployeeNotFoundException("Employee with id " + 3 + " not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 3L)
                        .contentType("application.json"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof EmployeeNotFoundException))
                .andExpect(result -> assertEquals("Employee with id " + 3 + " not found",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(employeeService).getById(3L);
    }

    @Test
    void gettingById_ShouldReturnEmployeeIfIdIsValid() throws Exception {
        when(employeeService.getById(4L)).thenReturn(VALID_EMPLOYEE);

        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", 4)
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_EMPLOYEE_DTO));

        verify(employeeService).getById(4L);
    }

    @Test
    void getAllEmployeesFromAnEmptyTable_ShouldReturnNoContent() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(204));

        verify(employeeService).getAllEmployees();
    }

    @Test
    void getAllEmployeesFromNotEmptyTable_ShouldReturnListOfEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(EMPLOYEE_LIST);

        final MvcResult mvcResult = mockMvc.perform(get("/employees")
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();
        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper
                .writeValueAsString(EMPLOYEE_LIST_DTO));

        verify(employeeService).getAllEmployees();
    }

    @Test
    void addEmployee_ShouldReturnAddedEmployee() throws Exception {
        when(employeeService.save(VALID_EMPLOYEE_ADD, 1L)).thenReturn(VALID_EMPLOYEE);

        final MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(VALID_EMPLOYEE_DTO))
                        .contentType("application/json"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_EMPLOYEE_DTO));

        verify(employeeService).save(VALID_EMPLOYEE_ADD, 1L);
    }

    @Test
    void addEmployee_ShouldReturnBadRequestIfIsInvalid() throws Exception {
        mockMvc.perform(post("/employees")
                        .content(objectMapper.writeValueAsString(INVALID_EMPLOYEE_DTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void editingEmployee_ShouldReturnNewEmployeeData() throws Exception {
        when(employeeService.update(4L, VALID_EMPLOYEE_ADD, 1L))
                .thenReturn(VALID_EMPLOYEE);

        final MvcResult mvcResult = mockMvc.perform(put("/employees/{id}", 4)
                        .content(objectMapper.writeValueAsString(VALID_EMPLOYEE_DTO))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_EMPLOYEE_DTO));

        verify(employeeService).update(4L, VALID_EMPLOYEE_ADD, 1L);
    }

    @Test
    void editingEmployee_ShouldReturnBadRequestIfDataIsInvalid() throws Exception {
        mockMvc.perform(put("/employees/{id}", 1L)
                        .content(objectMapper.writeValueAsString(INVALID_EMPLOYEE_DTO))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
