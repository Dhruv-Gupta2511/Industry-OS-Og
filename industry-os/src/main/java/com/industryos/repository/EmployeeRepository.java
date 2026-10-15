package com.industryos.repository;

import com.industryos.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByDeptId(Integer deptId);
    List<Employee> findByPlantId(Integer plantId);
    List<Employee> findByStatus(String status);
    long countByDeptId(Integer deptId);
    long countByDeptIdAndStatus(Integer deptId, String status);
}

