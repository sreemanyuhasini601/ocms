package com.reimbursement.controllers;

import com.reimbursement.models.SubDepartmentBudget;
import com.reimbursement.services.DepartmentBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-department-budgets")
@CrossOrigin(origins = { "http://localhost:3000" }, allowedHeaders = "*", allowCredentials = "true")
public class SubDepartmentBudgetController {

    @Autowired
    private DepartmentBudgetService departmentBudgetService;

    @GetMapping("/{departmentName}")
    public ResponseEntity<List<SubDepartmentBudget>> getSubDepartments(
            @PathVariable("departmentName") String departmentName) {
        return ResponseEntity.ok(departmentBudgetService.getSubDepartments(departmentName));
    }
}
