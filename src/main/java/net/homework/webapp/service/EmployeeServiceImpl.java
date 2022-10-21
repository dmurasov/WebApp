package net.homework.webapp.service;

import net.homework.webapp.exception.DepartmentNotFoundException;
import net.homework.webapp.exception.EmployeeNotFoundException;
import net.homework.webapp.model.Department;
import net.homework.webapp.model.Employee;
import net.homework.webapp.repository.DepartmentRepository;
import net.homework.webapp.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getById(final Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee with id " + id + " not found"));
        return employee;
    }

    @Transactional
    public Employee save(final Employee employee, final Long departmentId) {
        employee.setDepartment(departmentRepository.findById(departmentId).orElseThrow(
                () -> new DepartmentNotFoundException("Department with id " + departmentId + "not found")));
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee update(final Long id, Employee employee, final Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Employee with id " + departmentId + "not found"));

        Optional<Employee> findedEmployee = employeeRepository.findById(id);

        if(findedEmployee.isPresent()) {
            Employee updatedEmployee = findedEmployee.get();

            updatedEmployee.setFirstName(employee.getFirstName());
            updatedEmployee.setLastName(employee.getLastName());
            updatedEmployee.setDepartment(department);
            updatedEmployee.setEmail(employee.getEmail());
            updatedEmployee.setPhoneNumber(employee.getPhoneNumber());
            updatedEmployee.setSalary(employee.getSalary());

            employeeRepository.save(updatedEmployee);

            return employee;
        }else{
            throw new EmployeeNotFoundException("Employee with id " + id + "not found");
        }
    }
}
