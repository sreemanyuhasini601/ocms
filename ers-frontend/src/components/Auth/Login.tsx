import React, { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { FaUser, FaLock } from "react-icons/fa";
import { Icon } from "../Icon";
import "./Auth.css";
import { useGlobalData } from '../../globalData/store';
import { UserInterface } from "../../interfaces/UserInterface"

interface LoginProps {
    setUserRole: (role: string | null) => void;
}

export const Login: React.FC<LoginProps> = ({ setUserRole }) => {
    const [user, setUser] = useState<UserInterface>({ username: "", password: "", role: "" })
    const navigate = useNavigate();
    const { setGlobalData } = useGlobalData();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setUser(prev => ({ ...prev, [name]: value }));
    };

    useEffect(() => {
        const user = sessionStorage.getItem('user');
        if (user) {
            const parsedUser = JSON.parse(user);
            setUserRole(parsedUser.role);
            navigate(parsedUser.role === "manager" ? "/manager-dashboard" : "/employee-dashboard");
        }
    }, [setUserRole, navigate]);

    const login = async () => {
        try {
            const response = await axios.post("http://localhost:8080/users/login", user, { withCredentials: true });
            const { role, userId, username } = response.data;

            // 1. Update Global State
            setGlobalData(prev => ({ ...prev, user: { userId, username, role } }));

            // 2. Persist to Session Storage
            sessionStorage.setItem("user", JSON.stringify({ userId, username, role }));

            // 3. Update App Level Role State
            setUserRole(role);

            // 4. Navigate
            setTimeout(() => {
                navigate(role === "manager" ? "/manager-dashboard" : "/employee-dashboard");
            }, 100);

        } catch (error: unknown) {
            const message = axios.isAxiosError(error) && error.response?.data
                ? String(error.response.data)
                : "Login Failed! Check that the backend is running and try again.";
            alert(message);
        }
    };

    return (
        <div className="login">
            <div className="text-container">
                <h1>Welcome to the Employee Reimbursement System</h1>
                <h3>Sign in to manage your reimbursements!</h3>
                <div className="input-container">
                    <Icon icon={FaUser} className="icon" />
                    <input type="text" aria-label="Username" placeholder="Username" name="username" onChange={handleChange} />
                </div>
                <div className="input-container">
                    <Icon icon={FaLock} className="icon" />
                    <input type="password" aria-label="Password" placeholder="Password" name="password" onChange={handleChange} />
                </div>
                <button className="login-button" onClick={login}>Login</button>
                <p className="register-link">Don't have an account? <span onClick={() => navigate("/register")}>Sign up</span></p>
            </div>
        </div>
    );
};

export default Login;
