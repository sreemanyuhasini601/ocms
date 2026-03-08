package com.reimbursement.DAOs;

import com.reimbursement.models.ReimbursementRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReimbursementDAO extends JpaRepository<ReimbursementRequest, Integer> {
    @Query("SELECT r FROM ReimbursementRequest r LEFT JOIN FETCH r.user WHERE r.user.userId = :userId")
    List<ReimbursementRequest> findByUserUserId(@org.springframework.data.repository.query.Param("userId") int userId);

    @Query("SELECT r FROM ReimbursementRequest r LEFT JOIN FETCH r.user")
    List<ReimbursementRequest> findAllWithUser();

    @Query("SELECT COALESCE(SUM(r.amount), 0.0) FROM ReimbursementRequest r WHERE r.department = :department AND r.subDepartment = :subDepartment AND r.status = 'APPROVED'")
    Double getApprovedSumBySubDepartment(
            @org.springframework.data.repository.query.Param("department") String department,
            @org.springframework.data.repository.query.Param("subDepartment") String subDepartment);

    @Query("SELECT COALESCE(SUM(r.amount), 0.0) FROM ReimbursementRequest r WHERE r.department = :department AND r.status = 'APPROVED'")
    Double getApprovedSumByDepartment(@org.springframework.data.repository.query.Param("department") String department);
}
