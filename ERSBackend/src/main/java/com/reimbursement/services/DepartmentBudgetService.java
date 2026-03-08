package com.reimbursement.services;

import com.reimbursement.DAOs.DepartmentBudgetDAO;
import com.reimbursement.DAOs.SubDepartmentBudgetDAO;
import com.reimbursement.models.DepartmentBudget;
import com.reimbursement.DAOs.ReimbursementDAO;
import com.reimbursement.models.SubDepartmentBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentBudgetService {

    @Autowired
    private DepartmentBudgetDAO departmentBudgetDAO;

    @Autowired
    private SubDepartmentBudgetDAO subDepartmentBudgetDAO;

    @Autowired
    private ReimbursementDAO reimbursementDAO; // Inject ReimbursementDAO

    public List<DepartmentBudget> getAllBudgets() {
        List<DepartmentBudget> budgets = departmentBudgetDAO.findAll();
        for (DepartmentBudget budget : budgets) {
            // Calculate remaining dynamically
            Double approvedSum = reimbursementDAO.getApprovedSumByDepartment(budget.getDepartmentName());
            double newVal = Math.max(0, budget.getTotalBudget() - approvedSum);
            if (Math.abs(budget.getRemainingBudget() - newVal) > 0.01) { // optimize writes
                budget.setRemainingBudget(newVal);
                departmentBudgetDAO.save(budget);
            }
        }
        return budgets;
    }

    public List<SubDepartmentBudget> getSubDepartments(String departmentName) {
        System.out.println("Fetching sub-departments for: " + departmentName);
        List<SubDepartmentBudget> all = subDepartmentBudgetDAO.findAll();
        System.out.println("Total sub-departments found in DB: " + all.size());

        List<SubDepartmentBudget> filtered = all.stream()
                .filter(sd -> {
                    boolean match = sd.getDepartmentName().equals(departmentName);
                    if (!match)
                        System.out.println(
                                "Skipping: " + sd.getSubDepartmentName() + " (Dept: " + sd.getDepartmentName() + ")");
                    return match;
                })
                .map(sd -> {
                    // Recalculate remaining budget dynamically
                    Double approvedSum = reimbursementDAO.getApprovedSumBySubDepartment(sd.getDepartmentName(),
                            sd.getSubDepartmentName());
                    double newVal = Math.max(0, sd.getTotalBudget() - approvedSum);

                    // Update if different
                    if (Math.abs(sd.getRemainingBudget() - newVal) > 0.01) {
                        sd.setRemainingBudget(newVal);
                        subDepartmentBudgetDAO.save(sd); // Persist correction
                    }
                    return sd;
                })
                .toList();
        System.out.println("Returning " + filtered.size() + " sub-departments for " + departmentName);
        return filtered;
    }

    @Transactional
    public void deductBudget(String departmentName, String subDepartmentName, double amount) {
        // Redundant with dynamic calculation on read, but kept for immediate
        // consistency if needed before re-fetch.
        // Actually, strictly speaking, we don't need to manually deduct anymore if we
        // rely on the sum query.
        // But let's keep it as a "cache update" or just rely on the read logic.
        // To avoid double-counting confusion, we can simply DO NOTHING here or just
        // log,
        // because the next GET will sum up the Approved requests (including the one
        // just approved).
        System.out.println("Budget deduction handled dynamically via ReimbursementDAO aggregation.");
    }
}
