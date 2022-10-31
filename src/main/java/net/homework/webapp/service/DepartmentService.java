package net.homework.webapp.service;

import net.homework.webapp.model.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();

    Department getById(final Long id);

    Department update(final Long id, Department department);

    Department save(final Department department);
}
