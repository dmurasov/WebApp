package net.homework.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.homework.webapp.dto.DepartmentDto;
import net.homework.webapp.exception.DepartmentNotFoundException;
import net.homework.webapp.model.Department;
import net.homework.webapp.service.DepartmentService;
import net.homework.webapp.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class DepartmentControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private EmployeeService employeeService;
    public static final Department VALID_DEPARTMENT = Department.builder()
            .id(1L)
            .departmentName("IT")
            .location("Chisinau")
            .build();

    public static final DepartmentDto VALID_DEPARTMENT_DTO = DepartmentDto.builder()
            .id(1L)
            .departmentName("IT")
            .location("Chisinau")
            .build();

    public static final DepartmentDto INVALID_DEPARTMENT_DTO = DepartmentDto.builder()
            .id(5L)
            .departmentName("")
            .location("ff")
            .build();

    public static final DepartmentDto UPDATE_DEPARTMENT_DTO = DepartmentDto.builder()
            .id(3L)
            .departmentName("Finance")
            .location("Amsterdam")
            .build();

    public static final Department UPDATE_DEPARTMENT = Department.builder()
            .id(3L)
            .departmentName("Finance")
            .location("Amsterdam")
            .build();

    public static final List<DepartmentDto> DEPARTMENT_DTO_LIST = Arrays.asList(
            DepartmentDto.builder()
                    .id(1L)
                    .departmentName("Administration")
                    .location("Chisinau")
                    .build(),
            DepartmentDto.builder()
                    .id(2L)
                    .departmentName("HR")
                    .location("Iasi")
                    .build()
    );

    public static final List<Department> DEPARTMENT_LIST = Arrays.asList(
            Department.builder()
                    .id(1L)
                    .departmentName("Administration")
                    .location("Chisinau")
                    .build(),
            Department.builder()
                    .id(2L)
                    .departmentName("HR")
                    .location("Iasi")
                    .build()
    );

    @Test
    void gettingById_ShouldReturnDepartmentIfIdIsValid() throws Exception {
        when(departmentService.getById(1L)).thenReturn(VALID_DEPARTMENT);

        final MvcResult mvcResult = mockMvc.perform(get("/departments/{id}", 1)
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();

        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_DEPARTMENT_DTO));

        verify(departmentService).getById(1L);
    }

    @Test
    void getById_ShouldReturnNotFoundIfIdIsInvalid() throws Exception {
        when(departmentService.getById(4L)).thenThrow(new DepartmentNotFoundException("Department with id " + 4 + " not found"));

        mockMvc.perform(get("/departments/{id}", 4L)
                        .contentType("application.json"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DepartmentNotFoundException))
                .andExpect(result -> assertEquals("Department with id " + 4 + " not found",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));

        verify(departmentService).getById(4L);
    }

    @Test
    void getAllDepartmentsFromAnEmptyTable_ShouldReturnNoContent() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/departments"))
                .andExpect(status().is(204));

        verify(departmentService).getAllDepartments();
    }

    @Test
    void getAllDepartmentsFromNotEmptyTable_ShouldReturnListOfDepartments() throws Exception {
        when(departmentService.getAllDepartments()).thenReturn(DEPARTMENT_LIST);

        final MvcResult mvcResult = mockMvc.perform(get("/departments")
                        .contentType("application/json"))
                .andExpect(status().is(200))
                .andReturn();

        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(DEPARTMENT_DTO_LIST));

        verify(departmentService).getAllDepartments();
    }

    @Test
    void addDepartment_ShouldReturnAddedDepartment() throws Exception {
        when(departmentService.save(VALID_DEPARTMENT)).thenReturn(VALID_DEPARTMENT);

        final MvcResult mvcResult = mockMvc.perform(post("/departments")
                        .content(objectMapper.writeValueAsString(VALID_DEPARTMENT_DTO))
                        .contentType("application/json"))
                .andExpect(status().is(201))
                .andReturn();

        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(VALID_DEPARTMENT_DTO));
        verify(departmentService).save(VALID_DEPARTMENT);
    }

    @Test
    void addDepartment_ShouldReturnBadRequestIfIsInvalid() throws Exception {
        mockMvc.perform(post("/departments")
                        .content(objectMapper.writeValueAsString(INVALID_DEPARTMENT_DTO))
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    void editingAnDepartmentWithValidData_ShouldReturnNewDepartmentData() throws Exception {
        when(departmentService.update(3L, UPDATE_DEPARTMENT)).thenReturn(UPDATE_DEPARTMENT);

        final MvcResult mvcResult = mockMvc.perform(put("/departments/{id}", 3L)
                        .content(objectMapper.writeValueAsString(UPDATE_DEPARTMENT_DTO))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final String actualResponse = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(UPDATE_DEPARTMENT_DTO));

        verify(departmentService).update(3L, UPDATE_DEPARTMENT);
    }

    @Test
    void editingDepartment_ShouldReturnBadRequestIfDataIsInvalid() throws Exception {
        mockMvc.perform(put("/departments/{id}", 1L)
                        .content(objectMapper.writeValueAsString(INVALID_DEPARTMENT_DTO))
                        .contentType("application/json"))
                .andExpect(status().is(400))
                .andReturn();
    }
}