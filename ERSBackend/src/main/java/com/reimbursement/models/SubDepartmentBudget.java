package com.reimbursement.models;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_department_budgets", schema = "reimbursement_schema")
public class SubDepartmentBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "sub_department_name")
    private String subDepartmentName;

    @Column(name = "total_budget")
    private Double totalBudget;

    @Column(name = "remaining_budget")
    private Double remainingBudget;

    public SubDepartmentBudget() {
    }

    public SubDepartmentBudget(String departmentName, String subDepartmentName, Double totalBudget,
            Double remainingBudget) {
        this.departmentName = departmentName;
        this.subDepartmentName = subDepartmentName;
        this.totalBudget = totalBudget;
        this.remainingBudget = remainingBudget;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSubDepartmentName() {
        return subDepartmentName;
    }

    public void setSubDepartmentName(String subDepartmentName) {
        this.subDepartmentName = subDepartmentName;
    }

    public Double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Double getRemainingBudget() {
        return remainingBudget;
    }

    public void setRemainingBudget(Double remainingBudget) {
        this.remainingBudget = remainingBudget;
    }
}
