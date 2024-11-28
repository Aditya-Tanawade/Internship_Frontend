import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import WelcomePage from './components/WelcomePage/WelcomePage';
import Registration from './components/Registration/Registration';
import Home from './components/Home/Home';
import UserLogin from './components/Login/UserLogin';  // Import UserLogin component
import DoctorLogin from './components/Login/DoctorLogin';
import AdminLogin from './components/Login/AdminLogin';
import ForgotPassword from './components/Login/ForgotPassword';
import DoctorDashboard from './components/Dashborad/DoctorDashboard';
import EditProfile from './components/Dashborad/EditProfile';
import YourSchedule from './components/Dashborad/YourSchedule';
import UserDashboard from './components/UserDashboard/UserDashboard';
import CreateAppointment from './components/UserDashboard/CreateAppointment';
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

        <Route path="/doctor-dashboard" element={<DoctorDashboard />}/>

        <Route path="/edit-profile" element={<EditProfile />} />

        <Route path="/yourSchedule" element={<YourSchedule />} />

        <Route path="/User-dashboard" element={<UserDashboard />}/>

        <Route path="/CreateAppointment" element={<CreateAppointment />}/>




      </Routes>
    </BrowserRouter>
  );
}

export default App;
