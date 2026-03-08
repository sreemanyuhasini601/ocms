import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGlobalData } from '../../globalData/store';

interface LogoutProps {
    setUserRole: (role: string | null) => void;
}

const Logout: React.FC<LogoutProps> = ({ setUserRole }) => {
    const navigate = useNavigate();
    const { setGlobalData } = useGlobalData();

    useEffect(() => {
        // Clear user session
        sessionStorage.clear();

        // Clear Global Data
        setGlobalData(prev => ({ ...prev, user: null }));

        // Clear App State
        setUserRole(null);

        // Redirect to login
        navigate('/login');
    }, [navigate, setGlobalData, setUserRole]);

    return (
        <div className="logout">
            <h1>Logging out...</h1>
        </div>
    );
};

export default Logout;
