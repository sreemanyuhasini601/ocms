import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { FaMoneyBillAlt, FaAlignLeft } from 'react-icons/fa';
import { Icon } from '../Icon';
import './ReimbursementForm.css';
import { useGlobalData } from '../../globalData/store';

interface ReimbursementFormProps {
    onReimbursementSubmit: () => void;
    selectedDepartment: string;      // add this
    selectedSubDepartment: string;   // add this
}

export const ReimbursementForm: React.FC<ReimbursementFormProps> = ({
    onReimbursementSubmit,
    selectedDepartment,
    selectedSubDepartment
}) => {

    const [formData, setFormData] = useState({
        amount: '',
        description: '',
    });

    const { globalData } = useGlobalData();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            await axios.post(`${globalData.baseUrl}/api/reimbursements`, {
                amount: Number(formData.amount),
                description: formData.description,
                department: selectedDepartment,           // ✅ from props
                subDepartment: selectedSubDepartment,    // ✅ from props
            });
            alert('Reimbursement submitted successfully!');
            setFormData({ amount: '', description: '' });
            onReimbursementSubmit();
        } catch (error) {
            alert('Failed to submit reimbursement');
            console.error(error);
        }
    };

    return (
        <div className="form-container">
            <div className="form-content">
                <h1>Submit Your Reimbursement Request</h1>
                <div style={{ backgroundColor: '#f0f0f0', padding: '10px', marginBottom: '10px', fontSize: '12px' }}>
                    <strong>DEBUG INFO:</strong><br />
                    Selected Dept: {selectedDepartment}<br />
                    Selected SubDept: {selectedSubDepartment || "(EMPTY)"}
                </div>
                <p>Fill out the form below.</p>
                <form onSubmit={handleSubmit}>
                    <div className="input-container">
                        <Icon icon={FaMoneyBillAlt} className="icon" />
                        <input
                            type="number"
                            name="amount"
                            placeholder="Amount"
                            value={formData.amount}
                            onChange={handleChange}
                            required
                            min="1"
                            step="1"
                        />
                    </div>
                    <div className="input-container">
                        <Icon icon={FaAlignLeft} className="icon" />
                        <textarea
                            name="description"
                            placeholder="Description"
                            value={formData.description}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="input-container">
                        <label>
                            Department (budget to be approved from):{' '}
                            <select
                                name="department"
                                value={selectedDepartment}
                                onChange={handleChange}
                                required
                            >
                                <option value="IT">IT</option>
                                <option value="HR">HR</option>
                                <option value="Marketing">Marketing</option>
                            </select>
                        </label>
                    </div>
                    <button type="submit">Submit</button>
                </form>
            </div>
        </div>
    );
};
