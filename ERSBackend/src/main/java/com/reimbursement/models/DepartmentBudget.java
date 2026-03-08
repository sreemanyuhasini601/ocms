package com.reimbursement.models;

import jakarta.persistence.*;

@Entity
@Table(name = "department_budgets", schema = "reimbursement_schema")
public class DepartmentBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "department_name", nullable = false, unique = true, length = 50)
    private String departmentName;

    @Column(name = "total_budget", nullable = false)
    private double totalBudget;

    @Column(name = "remaining_budget", nullable = false)
    private double remainingBudget;

    public DepartmentBudget() {
    }

    public DepartmentBudget(String departmentName, double totalBudget, double remainingBudget) {
        this.departmentName = departmentName;
        this.totalBudget = totalBudget;
        this.remainingBudget = remainingBudget;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public double getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }
}
