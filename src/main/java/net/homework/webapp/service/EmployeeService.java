package net.homework.webapp.service;

import net.homework.webapp.model.Employee;
import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getById(final Long id);

    Employee update(final Long id, Employee employee, final Long departmentId);

    Employee save(final Employee employee, final Long departmentId);
}
