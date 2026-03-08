package com.reimbursement.services;

import com.reimbursement.DAOs.ReimbursementDAO;
import com.reimbursement.models.ReimbursementRequest;
import com.reimbursement.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReimbursementService {

    @Autowired
    private ReimbursementDAO reimbursementDAO;

    @Autowired
    private DepartmentBudgetService departmentBudgetService;

    public List<ReimbursementRequest> findAll() {
        return reimbursementDAO.findAllWithUser();
    }

    @Transactional
    public ReimbursementRequest createReimbursement(ReimbursementRequest request, Integer userId) {
        try {
            User user = new User();
            user.setUserId(userId); // No need to parse, userId is already an Integer
            request.setDateSubmitted(LocalDate.now());
            request.setStatus(ReimbursementRequest.ReimbursementStatus.PENDING);
            request.setUser(user);
            return reimbursementDAO.save(request);
        } catch (Exception e) {
            System.out.println("Error creating reimbursement: " + e.getMessage());
            throw e; // Rethrow the exception
        }
    }

    @Transactional
    public ReimbursementRequest approveReimbursement(int id) {
        Optional<ReimbursementRequest> found = reimbursementDAO.findById(id);
        if (found.isPresent()) {
            ReimbursementRequest request = found.get();
            request.setStatus(ReimbursementRequest.ReimbursementStatus.APPROVED);
            if (request.getDepartment() != null && !request.getDepartment().isBlank()) {
                departmentBudgetService.deductBudget(request.getDepartment(), request.getSubDepartment(),
                        request.getAmount());
            }
            return reimbursementDAO.save(request);
        } else {
            throw new RuntimeException("Reimbursement request not found");
        }
    }

    public ReimbursementRequest denyReimbursement(int id) {
        Optional<ReimbursementRequest> found = reimbursementDAO.findById(id);
        if (found.isPresent()) {
            ReimbursementRequest request = found.get();
            request.setStatus(ReimbursementRequest.ReimbursementStatus.DENIED);
            return reimbursementDAO.save(request);
        } else {
            throw new RuntimeException("Reimbursement request not found");
        }
    }

    public List<ReimbursementRequest> findByUserId(int userId) {
        return reimbursementDAO.findByUserUserId(userId);
    }
}
