package com.reimbursement.DAOs;

import com.reimbursement.models.SubDepartmentBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDepartmentBudgetDAO
                extends JpaRepository<SubDepartmentBudget, Long> {
        java.util.Optional<SubDepartmentBudget> findByDepartmentNameAndSubDepartmentName(String departmentName,
                        String subDepartmentName);
}
