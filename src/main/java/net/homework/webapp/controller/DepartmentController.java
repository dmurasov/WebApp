package net.homework.webapp.controller;

import net.homework.webapp.dto.DepartmentDto;
import net.homework.webapp.model.Department;
import net.homework.webapp.service.DepartmentService;
import net.homework.webapp.service.mapper.DepartmentMapper;
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

import static net.homework.webapp.service.mapper.DepartmentMapper.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> addDepartment(@RequestBody @Valid DepartmentDto departmentDto) {
        Department savedDepartment = departmentService
                .save(toDepartment(departmentDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fromDepartment(savedDepartment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable final Long id) {
        Department department = departmentService.getById(id);
        return ResponseEntity.ok(fromDepartment(department));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> departmentDtoList = fromDepartmentList(departmentService.getAllDepartments());

        if (CollectionUtils.isEmpty(departmentDtoList)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(departmentDtoList);
        }
        return ResponseEntity.ok(departmentDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable final Long id,
                                                          @Valid @RequestBody DepartmentDto departmentDto) {
        Department updatedDepartment = departmentService.update(id, toDepartment(departmentDto));
        if (updatedDepartment != null) {
            return ResponseEntity.ok(fromDepartment(updatedDepartment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
