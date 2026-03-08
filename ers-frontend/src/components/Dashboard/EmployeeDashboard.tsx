import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import { ReimbursementInterface } from '../../interfaces/ReimbursementInterface';
import { DepartmentBudgetInterface } from '../../interfaces/DepartmentBudgetInterface';
import { SubDepartmentBudgetInterface } from '../../interfaces/SubDepartmentBudgetInterface';
import { ReimbursementForm } from '../Reimbursement/ReimbursementForm';
import { ReimbursementList } from '../Reimbursement/ReimbursementList';
import './EmployeeDashboard.css';
import { useGlobalData } from '../../globalData/store';
import { Link, useNavigate } from "react-router-dom";

interface EmployeeDashboardProps {
    setUserRole: (role: string) => void;
}

const EmployeeDashboard: React.FC<EmployeeDashboardProps> = ({ setUserRole }) => {
    const [reimbursements, setReimbursements] = useState<ReimbursementInterface[]>([]);
    const [departmentBudgets, setDepartmentBudgets] = useState<DepartmentBudgetInterface[]>([]);
    const [selectedSubDepartment, setSelectedSubDepartment] = useState<string>('');
    const [subDepartmentBudgets, setSubDepartmentBudgets] = useState<SubDepartmentBudgetInterface[]>([]);
    const [subDepartments, setSubDepartments] = useState<string[]>([]);
    const [selectedDepartment, setSelectedDepartment] = useState<string>('IT');
    const [filters, setFilters] = useState({
        PENDING: true,
        APPROVED: true,
        DENIED: true
    });
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string>('');
    const { globalData } = useGlobalData();
    const navigate = useNavigate();
    const { setGlobalData } = useGlobalData();

    const fetchDepartmentBudgets = useCallback(async () => {
        try {
            const response = await axios.get<DepartmentBudgetInterface[]>(`${globalData.baseUrl}/api/department-budgets`);
            setDepartmentBudgets(response.data);
        } catch (err) {
            console.error('Failed to fetch department budgets', err);
        }
    }, [globalData.baseUrl]);

    const fetchReimbursements = useCallback(async () => {
        setIsLoading(true);
        // Always fetch budgets first or independently
        await fetchDepartmentBudgets();

        try {
            const user = sessionStorage.getItem('user');
            if (user) {
                const parsedUser = JSON.parse(user);
                const { userId, username, role } = parsedUser;
                setUserRole(role);
                setGlobalData(prev => ({ ...prev, user: { userId, username, role } }));

                const response = await axios.get<ReimbursementInterface[]>(`${globalData.baseUrl}/api/reimbursements/user/${userId}`);
                setReimbursements(response.data);
            }
        } catch (error: any) {
            console.error('Failed to fetch data', error);
            // Show more detailed error
            let errorMsg = error.message;
            if (error.response) {
                errorMsg = `Error ${error.response.status}: ${JSON.stringify(error.response.data)}`;
            }
            setError(`Failed to fetch reimbursement requests: ${errorMsg}`);
        } finally {
            setIsLoading(false);
        }
    }, [globalData.baseUrl, fetchDepartmentBudgets, setUserRole, setGlobalData]);

    const fetchSubDepartmentBudgets = useCallback(async () => {
        if (!selectedDepartment) return;
        try {
            const response = await axios.get<SubDepartmentBudgetInterface[]>(`${globalData.baseUrl}/api/sub-department-budgets/${selectedDepartment}`);
            setSubDepartmentBudgets(response.data);
            if (response.data.length > 0) {
                setSelectedSubDepartment(response.data[0].subDepartmentName);
            } else {
                setSelectedSubDepartment('');
            }
        } catch (err) {
            console.error('Failed to fetch sub-department budgets', err);
            setSubDepartmentBudgets([]);
            setSelectedSubDepartment('');
        }
    }, [globalData.baseUrl, selectedDepartment]);

    useEffect(() => {
        console.log("Effect triggered: Fetching sub-depts for", selectedDepartment);
        fetchSubDepartmentBudgets();
    }, [fetchSubDepartmentBudgets]);

    useEffect(() => {
        fetchReimbursements();
    }, [fetchReimbursements]);

    const handleCheckboxChange = (status: keyof typeof filters) => {
        setFilters(prev => ({ ...prev, [status]: !prev[status] }));
    };

    const filteredReimbursements = reimbursements.filter(reimbursement =>
        Object.entries(filters).some(([status, checked]) => checked && reimbursement.status === status)
    );

    const selectedBudget = departmentBudgets.find(b => b.departmentName === selectedDepartment);

    return (
        <div className="employee-dashboard">
            <h1>Employee Dashboard</h1>
            <section className="constant-budget-section">
                <h2>Constant budget</h2>
                <label>
                    Department:{' '}
                    <select
                        value={selectedDepartment}
                        onChange={(e) => setSelectedDepartment(e.target.value)}
                        className="department-select"
                    >
                        <option value="IT">IT</option>
                        <option value="HR">HR</option>
                        <option value="Marketing">Marketing</option>
                    </select>
                </label>
                <label style={{ marginLeft: '1rem' }}>
                    Sub-Department:{' '}
                    <select
                        value={selectedSubDepartment}
                        onChange={(e) => setSelectedSubDepartment(e.target.value)}
                        className="department-select"
                    >
                        {subDepartmentBudgets.map(sub => (
                            <option key={sub.id} value={sub.subDepartmentName}>{sub.subDepartmentName}</option>
                        ))}
                    </select>
                </label>
                {selectedBudget && (
                    <div className="budget-display">
                        <p><strong>Department Constant:</strong> ${selectedBudget.totalBudget.toLocaleString()}</p>
                        <p><strong>Department Remaining:</strong> ${selectedBudget.remainingBudget.toLocaleString()}</p>
                    </div>
                )}
                {(() => {
                    const selectedSubBudget = subDepartmentBudgets.find(b => b.subDepartmentName === selectedSubDepartment);
                    return selectedSubBudget ? (
                        <div className="budget-display" style={{ marginTop: '10px', borderTop: '1px solid #ccc', paddingTop: '10px' }}>
                            <p><strong>{selectedSubBudget.subDepartmentName} Constant:</strong> ${selectedSubBudget.totalBudget.toLocaleString()}</p>
                            <p><strong>{selectedSubBudget.subDepartmentName} Remaining:</strong> ${selectedSubBudget.remainingBudget.toLocaleString()}</p>
                        </div>
                    ) : null;
                })()}
            </section>
            <ReimbursementForm
                onReimbursementSubmit={fetchReimbursements}
                selectedDepartment={selectedDepartment}
                selectedSubDepartment={selectedSubDepartment}
            />

            <div>
                {Object.keys(filters).map(status => (
                    <label key={status}>
                        <input
                            type="checkbox"
                            checked={filters[status as keyof typeof filters]}
                            onChange={() => handleCheckboxChange(status as keyof typeof filters)}
                        />
                        Show {status}
                    </label>
                ))}
            </div>
            <h2>Your Reimbursement Requests</h2>
            {isLoading ? (
                <p>Loading...</p>
            ) : error ? (
                <p>{error}</p>
            ) : (
                <ReimbursementList reimbursements={filteredReimbursements} />
            )}
            <p className="register-link"><Link to="/logout">Logout</Link></p>
        </div>
    );
};

export default EmployeeDashboard;
