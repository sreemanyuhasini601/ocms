package com.reimbursement.controllers;

import com.reimbursement.models.ReimbursementRequest;
import com.reimbursement.services.ReimbursementService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reimbursements")
@CrossOrigin(origins = { "http://localhost:3000" }, allowedHeaders = "*", allowCredentials = "true")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;

    @GetMapping
    public ResponseEntity<?> getAllReimbursements() {
        try {
            return ResponseEntity.ok(reimbursementService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve reimbursements");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReimbursementsByUserId(@PathVariable("userId") int userId) {
        try {
            System.out.println("Fetching reimbursements for User ID: " + userId);
            var results = reimbursementService.findByUserId(userId);
            System.out.println("Found " + results.size() + " reimbursements for User ID: " + userId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reimbursements for user with ID " + userId + " not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> createReimbursement(@RequestBody ReimbursementRequest request, HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired or user not logged in.");
            }
            System.out.println("Received Reimbursement Creation Request:");
            System.out.println("Amount: " + request.getAmount());
            System.out.println("Dept: " + request.getDepartment());
            System.out.println("SubDept: " + request.getSubDepartment());
            System.out.println("User ID: " + userId);

            return ResponseEntity.ok(reimbursementService.createReimbursement(request, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create reimbursement: " + e.getMessage());
        }
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveReimbursement(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(reimbursementService.approveReimbursement(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to approve reimbursement");
        }
    }

    @PostMapping("/deny/{id}")
    public ResponseEntity<?> denyReimbursement(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(reimbursementService.denyReimbursement(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to deny reimbursement");
        }
    }
}
