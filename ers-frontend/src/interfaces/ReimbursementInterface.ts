// `ReimbursementInterface` 
export interface ReimbursementInterface {
    reimbursementId?: number;
    amount: number;
    description: string;
    department?: string; // IT, HR, or Marketing - which department's budget to deduct from on approval
    subDepartment?: string; // Technical, Non-Technical, Emergency
    status: 'Pending' | 'Approved' | 'Denied' | 'PENDING' | 'APPROVED' | 'DENIED';
    userId: number;
    dateSubmitted?: Date;
}
