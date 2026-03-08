package com.reimbursement.controllers;

import com.reimbursement.DAOs.DepartmentBudgetDAO;
import com.reimbursement.DAOs.SubDepartmentBudgetDAO;
import com.reimbursement.models.DepartmentBudget;
import com.reimbursement.models.SubDepartmentBudget;
import com.reimbursement.models.ReimbursementRequest;
import com.reimbursement.DAOs.ReimbursementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private com.reimbursement.services.UserService userService;

    @Autowired
    private com.reimbursement.DAOs.UserDAO userDAO;

    @Autowired
    private DepartmentBudgetDAO departmentBudgetDAO;

    @Autowired
    private SubDepartmentBudgetDAO subDepartmentBudgetDAO;

    @Autowired
    private ReimbursementDAO reimbursementDAO;

    @GetMapping("/dump")
    public ResponseEntity<Map<String, Object>> dumpDatabase() {
        Map<String, Object> data = new HashMap<>();

        List<DepartmentBudget> departments = departmentBudgetDAO.findAll();
        data.put("departments_count", departments.size());
        data.put("departments_raw", departments);

        List<SubDepartmentBudget> subDepartments = subDepartmentBudgetDAO.findAll();
        data.put("sub_departments_count", subDepartments.size());
        data.put("sub_departments_raw", subDepartments);

        List<ReimbursementRequest> reimbursements = reimbursementDAO.findAll();
        data.put("reimbursements_count", reimbursements.size());
        data.put("reimbursements_raw", reimbursements);

        List<com.reimbursement.models.User> users = userDAO.findAll();
        data.put("users_count", users.size());
        data.put("users_usernames", users.stream().map(com.reimbursement.models.User::getUsername).toList());

        return ResponseEntity.ok(data);
    }

    @GetMapping("/cleanup")
    public ResponseEntity<String> cleanupSeededData() {
        List<String> toRemove = List.of(
                "IT Support", "Development", "QA",
                "Recruiting", "Employee Relations",
                "Sales", "Social Media", "Content");

        int count = 0;
        List<SubDepartmentBudget> all = subDepartmentBudgetDAO.findAll();
        for (SubDepartmentBudget sd : all) {
            if (toRemove.contains(sd.getSubDepartmentName())) {
                subDepartmentBudgetDAO.delete(sd);
                count++;
            }
        }

        return ResponseEntity.ok("Deleted " + count + " seeded rows.");
    }

    @GetMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUserByName(@PathVariable("username") String username) {
        try {
            com.reimbursement.models.User user = userService.findByUsername(username);
            if (user != null) {
                userService.deleteUser(user.getUserId());
                return ResponseEntity.ok("User '" + username + "' deleted successfully. You can now Register again.");
            } else {
                return ResponseEntity.ok("User '" + username + "' does not exist (already deleted).");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete user: " + e.getMessage());
        }
    }

    @GetMapping("/seed")
    public ResponseEntity<String> seedDatabase() {
        int deptCount = 0;
        int subDeptCount = 0;

        // Seed Departments
        if (departmentBudgetDAO.findByDepartmentName("IT").isEmpty()) {
            DepartmentBudget it = new DepartmentBudget();
            it.setDepartmentName("IT");
            it.setTotalBudget(100000);
            it.setRemainingBudget(100000);
            departmentBudgetDAO.save(it);
            deptCount++;
        }
        if (departmentBudgetDAO.findByDepartmentName("HR").isEmpty()) {
            DepartmentBudget hr = new DepartmentBudget();
            hr.setDepartmentName("HR");
            hr.setTotalBudget(50000);
            hr.setRemainingBudget(50000);
            departmentBudgetDAO.save(hr);
            deptCount++;
        }
        if (departmentBudgetDAO.findByDepartmentName("Marketing").isEmpty()) {
            DepartmentBudget mkt = new DepartmentBudget();
            mkt.setDepartmentName("Marketing");
            mkt.setTotalBudget(75000);
            mkt.setRemainingBudget(75000);
            departmentBudgetDAO.save(mkt);
            deptCount++;
        }

        // Seed Sub-Departments
        String[][] subDepts = {
                { "IT", "Technical", "50000" }, { "IT", "Support", "30000" },
                { "HR", "Recruiting", "20000" }, { "HR", "Employee Relations", "15000" },
                { "Marketing", "Social Media", "25000" }, { "Marketing", "Sales", "40000" }
        };

        for (String[] sd : subDepts) {
            String deptName = sd[0];
            String subName = sd[1];
            double budget = Double.parseDouble(sd[2]);

            // Check if exists (simplified check, assumes unique names globally or checks
            // list)
            boolean exists = subDepartmentBudgetDAO.findAll().stream()
                    .anyMatch(s -> s.getSubDepartmentName().equals(subName) && s.getDepartmentName().equals(deptName));

            if (!exists) {
                SubDepartmentBudget sdb = new SubDepartmentBudget();
                sdb.setDepartmentName(deptName);
                sdb.setSubDepartmentName(subName);
                sdb.setTotalBudget(budget);
                sdb.setRemainingBudget(budget);
                subDepartmentBudgetDAO.save(sdb);
                subDeptCount++;
            }
        }

        return ResponseEntity.ok("Seeded " + deptCount + " Departments and " + subDeptCount + " Sub-Departments.");
    }
}
