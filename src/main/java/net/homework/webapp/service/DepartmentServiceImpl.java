package net.homework.webapp.service;

import net.homework.webapp.model.Department;
import net.homework.webapp.repository.DepartmentRepository;
import net.homework.webapp.exception.DepartmentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Department getById(final Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        if (!department.isPresent()) {
            throw new DepartmentNotFoundException("Department with id " + id + " not found");
        }
        return department.get();
    }

    @Transactional
    public Department save(final Department department) {
        return departmentRepository.save(department);
    }

    @Transactional
    public Department update(final Long id, final Department department) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);

        if (departmentOptional.isPresent()) {
            Department findedDepartment = departmentOptional.get();
            findedDepartment.setDepartmentName(department.getDepartmentName());
            findedDepartment.setLocation(department.getLocation());
            return departmentRepository.save(findedDepartment);
        } else {
            throw new DepartmentNotFoundException("Department with id " + id + "not found");
        }
    }
}
