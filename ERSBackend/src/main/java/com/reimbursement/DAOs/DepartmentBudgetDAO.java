package com.reimbursement.DAOs;

import com.reimbursement.models.DepartmentBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentBudgetDAO extends JpaRepository<DepartmentBudget, Integer> {
    Optional<DepartmentBudget> findByDepartmentName(String departmentName);
}
