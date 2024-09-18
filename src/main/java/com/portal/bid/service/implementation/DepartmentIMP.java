package com.portal.bid.service.implementation;

import com.portal.bid.entity.Department;
import com.portal.bid.repository.DepartmentRepository;
import com.portal.bid.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class DepartmentIMP implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department dep) {
        dep.setCreatedAt(LocalDateTime.now()); // Set the createdAt timestamp when creating a new department
        return departmentRepository.save(dep);
    }

    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department updateDepartment(Department dep) {
        Optional<Department> existingDepartment = departmentRepository.findById(dep.getId());
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setDep_name(dep.getDep_name());
            updatedDepartment.setStatus(dep.getStatus());
            updatedDepartment.setUpdatedAt(LocalDateTime.now()); // Set the updatedAt timestamp when updating a department
            return departmentRepository.save(updatedDepartment);
        }
        return null;
    }

    @Override
    public boolean deleteDepartment(Long id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
