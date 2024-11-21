import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import WelcomePage from './components/WelcomePage/WelcomePage';
import Registration from './components/Registration/Registration';
import Home from './components/Home/Home';
import UserLogin from './components/Login/UserLogin';  // Import UserLogin component
import DoctorLogin from './components/Login/DoctorLogin';
import AdminLogin from './components/Login/AdminLogin';
import ForgotPassword from './components/Login/ForgotPassword';


function App() {
  return (
    <BrowserRouter>
      <Routes>
      <Route path="/" element={<Home />} />
        <Route path="/WelcomePage" element={<WelcomePage />} />  {/* WelcomePage route */}
        
        {/* User-specific login */}
        <Route path="/user-login" element={<UserLogin />} />  {/* User Login route */}
        
        {/* Admin login */}
        <Route path="/admin-login" element={<AdminLogin />} />  {/* Admin Login route */}
        
        {/* Doctor login */}
        <Route path="/doctor-login" element={<DoctorLogin />} />  {/* Doctor Login route */}
        
        <Route path="/registration" element={<Registration />} />  {/* Registration route */}

        <Route path="/forgot-password" element={<ForgotPassword />}/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
