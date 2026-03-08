package com.reimbursement.controllers;

import com.reimbursement.models.DepartmentBudget;
import com.reimbursement.services.DepartmentBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department-budgets")
@CrossOrigin(origins = { "http://localhost:3000" }, allowedHeaders = "*", allowCredentials = "true")
public class DepartmentBudgetController {

    @Autowired
    private DepartmentBudgetService departmentBudgetService;

    @GetMapping
    public ResponseEntity<List<DepartmentBudget>> getAllBudgets() {
        return ResponseEntity.ok(departmentBudgetService.getAllBudgets());
    }
}
