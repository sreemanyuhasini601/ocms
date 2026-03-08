package com.reimbursement;

import com.reimbursement.DAOs.DepartmentBudgetDAO;
import com.reimbursement.DAOs.SubDepartmentBudgetDAO;
import com.reimbursement.models.DepartmentBudget;
import com.reimbursement.models.SubDepartmentBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataResetRunner implements CommandLineRunner {

    @Autowired
    private DepartmentBudgetDAO departmentBudgetDAO;

    @Autowired
    private SubDepartmentBudgetDAO subDepartmentBudgetDAO;

    @Autowired
    private com.reimbursement.DAOs.ReimbursementDAO reimbursementDAO;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running Data Reset...");

        System.out.println("Clearing all previous Reimbursement Requests...");
        reimbursementDAO.deleteAll();

        // Reset IT Department Budget
        Optional<DepartmentBudget> itBudget = departmentBudgetDAO.findByDepartmentName("IT");
        if (itBudget.isPresent()) {
            DepartmentBudget budget = itBudget.get();
            if (budget.getRemainingBudget() != budget.getTotalBudget()) {
                System.out.println(
                        "Resetting IT Budget from " + budget.getRemainingBudget() + " to " + budget.getTotalBudget());
                budget.setRemainingBudget(budget.getTotalBudget());
                departmentBudgetDAO.save(budget);
                System.out.println("IT Budget Reset Complete.");
            } else {
                System.out.println("IT Budget is already correct.");
            }
        }

        // Reset Technical Sub-Department Budget (Assuming under IT)
        Optional<SubDepartmentBudget> techBudget = subDepartmentBudgetDAO.findByDepartmentNameAndSubDepartmentName("IT",
                "Technical");
        if (techBudget.isPresent()) {
            SubDepartmentBudget budget = techBudget.get();
            if (budget.getRemainingBudget() != budget.getTotalBudget()) {
                System.out.println("Resetting Technical Sub-Department Budget from " + budget.getRemainingBudget()
                        + " to " + budget.getTotalBudget());
                budget.setRemainingBudget(budget.getTotalBudget());
                subDepartmentBudgetDAO.save(budget);
                System.out.println("Technical Sub-Department Budget Reset Complete.");
            } else {
                System.out.println("Technical Sub-Department Budget is already correct.");
            }
        } else {
            System.out.println("Technical Sub-Department not found under IT.");
        }
    }
}
