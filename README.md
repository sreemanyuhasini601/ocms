# Employee Reimbursement System (ERS)

## Overview
Employee Reimbursement System is a full-stack web application that digitizes the employee expense reimbursement workflow.

It replaces manual paper-based processes with a centralized platform where employees submit reimbursement requests and managers review, approve, or deny them through role-based dashboards.

## Key Idea
- Takes employee reimbursement requests as input
- Processes them through a Spring Boot backend with business validations
- Tracks department and sub-department budgets dynamically
- Provides separate dashboards for Employees and Managers
- Displays real-time status updates and budget visibility

## Features

### Authentication
- User registration and login
- Session-based authentication
- Role-based protected routes

### Employee Dashboard
- Submit reimbursement requests with amount, description, and department
- View and filter personal tickets by status
- Update description of pending requests
- View department budget remaining

### Manager Dashboard
- View all reimbursements across the organization
- Approve or deny pending requests
- View, delete, and promote users

### Budget Management
- Department-level budget tracking (IT, HR, Marketing)
- Sub-department-level budget breakdowns
- Dynamic budget recalculation on approval

## 🛠 Tech Stack

### Frontend
- React.js
- TypeScript
- Axios
- React Router

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate ORM

### Database
- PostgreSQL

### Tools
- VS Code
- Maven
- Node.js

## How It Works
1. User registers and logs in
2. Based on role, the app renders Employee or Manager dashboard
3. Employee submits a reimbursement → stored as PENDING
4. Manager reviews and approves or denies the request
5. Approved amounts are deducted from department budgets automatically

## Core Engineering Logic

### Layered Architecture
The backend follows a clean 3-tier structure:
- Controllers → handle HTTP requests
- Services → contain business logic
- DAOs → interact with the database via JPA

### Dynamic Budget Calculation
Budget data is not static — approved reimbursements continuously affect balances.
To handle this, the system uses a dynamic recalculation approach:
- Primary source: aggregation query on approved reimbursements
- Fallback: stored remaining budget field, updated as cache on each read

This ensures the application always produces accurate budget data.

### Role-Based Access
- User role is stored in both HTTP session (server) and sessionStorage (client)
- React Router renders the correct dashboard based on role
- Unauthenticated users are redirected to login

### Cascading Data Integrity
When a manager deletes a user, all associated reimbursement requests are automatically removed via JPA's CascadeType.ALL — preserving referential integrity.

## Future Improvements
- Password hashing with BCrypt
- Email notifications on status changes
- Analytics dashboard with spending charts
- Receipt upload support
- Cloud deployment with CI/CD

## Author
Built as a full-stack reimbursement management tool that converts manual expense approval workflows into a digital, role-based platform with real-time budget tracking.
