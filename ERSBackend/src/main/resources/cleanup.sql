-- SQL Command to delete the unwanted auto-seeded sub-departments

DELETE FROM reimbursement_schema.sub_department_budgets 
WHERE sub_department_name IN ('IT Support', 'Development', 'QA');

-- If you also want to remove the seeded HR and Marketing sub-departments:

-- If you want to DELETE 'hasini' so you can Register/Sign Up again:
DELETE FROM reimbursement_schema.users WHERE username = 'hasini';
