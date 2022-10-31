package net.homework.webapp.service.mapper;

import net.homework.webapp.dto.DepartmentDto;
import net.homework.webapp.model.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentMapper {

    public static Department toDepartment(final DepartmentDto departmentDto) {
        return Department.builder()
                .id(departmentDto.getId())
                .departmentName(departmentDto.getDepartmentName())
                .location(departmentDto.getLocation())
                .build();
    }

    public static DepartmentDto fromDepartment(final Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .location(department.getLocation())
                .build();
    }

    public static List<Department> toDepartmentList(final List<DepartmentDto> departmentDtoList) {
        List<Department> departmentList = new ArrayList<>();
        departmentDtoList.forEach(
                departmentDto -> departmentList.add(toDepartment(departmentDto)));
        return departmentList;
    }

    public static List<DepartmentDto> fromDepartmentList(final List<Department> departmentList) {
        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        departmentList.forEach(
                department -> departmentDtoList.add(fromDepartment(department)));
        return departmentDtoList;
    }
}
