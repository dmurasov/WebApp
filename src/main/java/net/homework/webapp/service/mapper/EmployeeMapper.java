package net.homework.webapp.service.mapper;

import net.homework.webapp.dto.DepartmentDto;
import net.homework.webapp.dto.EmployeeDto;
import net.homework.webapp.model.Department;
import net.homework.webapp.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeMapper {

    public static Employee toEmployee(final EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .phoneNumber(employeeDto.getPhoneNumber())
                .salary(employeeDto.getSalary())
                .build();
    }

    public static EmployeeDto fromEmployee(final Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .departmentId(employee.getDepartment().getId())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .salary(employee.getSalary())
                .build();
    }

    public static List<Employee> toemployeeList(final List<EmployeeDto> employeeDtoList) {
        List<Employee> employeeList = new ArrayList<>();
        employeeDtoList.forEach(
                employeeDto -> employeeList.add(toEmployee(employeeDto)));
        return employeeList;
    }

    public static List<EmployeeDto> fromEmployeeList(final List<Employee> employeeList) {
        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        employeeList.forEach(
                employee -> employeeDtoList.add(fromEmployee(employee)));
        return employeeDtoList;
    }
}
