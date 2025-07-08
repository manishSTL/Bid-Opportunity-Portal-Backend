package com.portal.bid.service;

import com.portal.bid.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department saveDepartment(Department dep);

    List<Department> getAllDepartment();

    Department updateDepartment(Department dep);

    boolean deleteDepartment(Long id);
}
