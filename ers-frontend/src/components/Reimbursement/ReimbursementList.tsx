import React from 'react';
import { ReimbursementInterface } from '../../interfaces/ReimbursementInterface';
import './ReimbursementList.css';

interface ReimbursementListProps {
    reimbursements: ReimbursementInterface[];
}

export const ReimbursementList: React.FC<ReimbursementListProps> = ({ reimbursements }) => {
    return (
        <div className="reimbursement-list">
            {reimbursements.length > 0 ? (
                <ul>
                    {reimbursements.map((item, index) => (
                        <li key={index}>{item.description} - ${item.amount} {item.department && `[${item.department}]`} ({item.status})</li>
                    ))}
                </ul>
            ) : (
                <p>No reimbursement requests to display.</p>
            )}
        </div>
    );
};
