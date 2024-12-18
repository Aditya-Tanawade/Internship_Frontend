import React, { useState, useEffect } from "react";
import axios from "axios";
import "./AdminDashboard.css";
import UserManagement from "./UserManagement/UserManagement";
import DoctorManagement from "./DoctorManagement/DoctorManagement";
import AppointmentManagement from "./AppointmentManagement/AppointmentManagement";
import PatientManagement from "./PatientManagement/PatientManagement";

const AdminDashboard = () => {
  const [selectedMenu, setSelectedMenu] = useState("Dashboard");
  const [adminEmail, setAdminEmail] = useState("");

  // Fetch admin email on load
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/admin/get-welcome-email")
      .then((response) => {
        setAdminEmail(response.data.email || "Unknown Email");
      })
      .catch((error) => {
        console.error("Error fetching admin email:", error);
        setAdminEmail("Error fetching email");
      });
  }, []);

  const handleMenuClick = (menu) => {
    setSelectedMenu(menu);
  };

  return (
    <div className="dashboard-container">
      {/* Sidebar */}
      <div className="sidebar">
        <div className="profile-section">
          <img
            src="assets/img/admin.png"
            alt="Admin"
            className="profile-picture"
          />
          <p className="admin-name">Admin</p>
        </div>
        <ul className="menu-list">
          {["Dashboard", "Users", "Doctors", "Appointments", "Patients", "Settings"].map((menu) => (
            <li
              key={menu}
              className={selectedMenu === menu ? "menu-item active" : "menu-item"}
              onClick={() => handleMenuClick(menu)}
            >
              {menu}
            </li>
          ))}
        </ul>
      </div>

      {/* Main Content */}
      <div className="main-content">
        <div className="admin-navbar">
          <ul>
            <li>
              <span>Welcome {adminEmail || "Loading..."}</span>
            </li>
            <li>
              <a href="/WelcomePage">Logout</a>
            </li>
          </ul>
        </div>

        {/* Dashboard Cards */}
        {selectedMenu === "Dashboard" && (
          <div className="cards-container">
            <div className="card">
              <h2>Total Users</h2>
              <p>100</p>
            </div>
            <div className="card">
              <h2>Total Doctors</h2>
              <p>20</p>
            </div>
            <div className="card">
              <h2>Appointments</h2>
              <p>50</p>
            </div>
          </div>
        )}

        {/* Dynamic Components */}
        {selectedMenu === "Users" && <UserManagement />}
        {selectedMenu === "Doctors" && <DoctorManagement />}
        {selectedMenu === "Appointments" && <AppointmentManagement />}
        {selectedMenu === "Patients" && <PatientManagement />}
      </div>
    </div>
  );
};

export default AdminDashboard;
