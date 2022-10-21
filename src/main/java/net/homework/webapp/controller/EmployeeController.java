package net.homework.webapp.controller;

import net.homework.webapp.dto.EmployeeDto;
import net.homework.webapp.model.Employee;
import net.homework.webapp.service.EmployeeService;
import net.homework.webapp.service.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static net.homework.webapp.service.mapper.EmployeeMapper.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employeeListDto = fromEmployeeList(employeeService.getAllEmployees());

        if (CollectionUtils.isEmpty(employeeListDto)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(employeeListDto);
        }
        return ResponseEntity.ok(employeeListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable final Long id) {
        Employee employee = employeeService.getById(id);
        return ResponseEntity.ok(fromEmployee(employee));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> addEmployee(@Valid @RequestBody final EmployeeDto employeeDto) {
        Employee employeeSaved = employeeService
                .save(toEmployee(employeeDto), employeeDto.getDepartmentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(fromEmployee(employeeSaved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable final Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        Employee updateEmployee = employeeService
                .update(id, toEmployee(employeeDto), employeeDto.getDepartmentId());

        if(updateEmployee == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(fromEmployee(updateEmployee));
    }
}