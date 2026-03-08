// App.tsx

import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Login } from './components/Auth/Login';
import Logout from './components/Auth/Logout';
import { Register } from './components/Auth/Register';
import EmployeeDashboard from './components/Dashboard/EmployeeDashboard';
import ManagerDashboard from './components/Dashboard/ManagerDashboard';
import { UserProvider } from './components/UserContext';
import { useGlobalData } from './globalData/store';
import './App.css';

const App: React.FC = () => {
  const [role, setRole] = useState<string | null>(null);
  const { globalData, setGlobalData } = useGlobalData();

  // Initialize state from sessionStorage on mount
  useEffect(() => {
    const storedUser = sessionStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      setRole(parsedUser.role);
      // Ensure global data is also synced if it wasn't already
      if (!globalData.user) {
        setGlobalData(prev => ({ ...prev, user: parsedUser }));
      }
    }
  }, [setGlobalData, globalData.user]);

  const isAuthenticated = !!role;

  const renderMainRoute = () => {
    if (!isAuthenticated) {
      return <Navigate to="/login" />;
    }
    return role === 'manager' ? <Navigate to="/manager-dashboard" /> : <Navigate to="/employee-dashboard" />;
  };

  return (
    <div className='App'>
      <UserProvider>
        <Router>
          <Routes>
            <Route path="/login" element={<Login setUserRole={setRole} />} />
            <Route path="/logout" element={<Logout setUserRole={setRole} />} />
            <Route path="/register" element={<Register />} />
            {/* Protect these routes */}
            <Route path="/employee-dashboard" element={isAuthenticated ? <EmployeeDashboard setUserRole={setRole} /> : <Navigate to="/login" />} />
            <Route path="/manager-dashboard" element={isAuthenticated ? <ManagerDashboard /> : <Navigate to="/login" />} />
            <Route path="/" element={renderMainRoute()} />
          </Routes>
        </Router>
      </UserProvider>
    </div>
  );
}

export default App;
