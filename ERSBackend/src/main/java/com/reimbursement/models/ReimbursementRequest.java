package com.reimbursement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reimbursement_requests")
public class ReimbursementRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reimbursement_id")
    private int reimbursementId;

    @Column(nullable = false)
    private double amount;

    @Column(length = 500)
    private String description;

    @Column(name = "department", length = 50)
    private String department; // IT, HR, or Marketing - which department's budget to deduct from on approval

    @Column(name = "sub_department", length = 50)
    private String subDepartment;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReimbursementStatus status; // "pending", "approved", "denied"

    @Column(nullable = false)
    private LocalDate dateSubmitted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public ReimbursementRequest() {
    }

    public int getReimbursementId() {
        return reimbursementId;
    }

    public void setReimbursementId(int reimbursementId) {
        this.reimbursementId = reimbursementId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(String subDepartment) {
        this.subDepartment = subDepartment;
    }

    public ReimbursementStatus getStatus() {
        return status;
    }

    public void setStatus(ReimbursementStatus status) {
        this.status = status;
    }

    public LocalDate getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(LocalDate dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Exposes the submitter's username for API responses (employee who sent the
     * request).
     */
    public String getSubmitterUsername() {
        return user != null ? user.getUsername() : null;
    }

    @Override
    public String toString() {
        return "ReimbursementRequest{" +
                "reimbursementId=" + reimbursementId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", department='" + department + '\'' +
                ", subDepartment='" + subDepartment + '\'' +
                ", status='" + status + '\'' +
                ", dateSubmitted=" + dateSubmitted +
                ", user=" + user +
                '}';
    }

    public enum ReimbursementStatus {
        PENDING, APPROVED, DENIED
    }
}
