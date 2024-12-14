// src/components/DoctorDashboard.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import "./DoctorDashboard.css";
import EditProfile from "../EditDoctorProfile/EditProfile";
import YourSchedule from "../UpdateAvialblity/YourSchedule"; 
import ApproveAppointments from "../ApproveAppoinments/ApproveAppointments"; // Import the new component
import Appointments from "../ApproveAppoinments/Appointments"; // Import the Appointments component



const DoctorDashboard = () => {
  const [selectedMenu, setSelectedMenu] = useState("Dashboard");
  const [doctorEmail, setDoctorEmail] = useState("");
  const [doctorProfile, setDoctorProfile] = useState({
    doctorName: "",
    speciality: "",
    location: "",
    mobileNo: "",
    hospitalName: "",
    chargedPerVisit: "",
  });

  // Fetch doctor's email
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/doctor/get-welcome-email")
      .then((response) => {
        setDoctorEmail(response.data.email || "Unknown Email");
      })
      .catch((error) => {
        console.error("Error fetching email:", error);
        setDoctorEmail("Error fetching email");
      });
  }, []);

  // Fetch doctor's profile
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/doctor/profile")
      .then((response) => {
        setDoctorProfile(response.data); // Populate doctorProfile with backend data
      })
      .catch((error) => {
        console.error("Error fetching profile:", error);
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
            src="assets/img/maledoctor.png"
            alt="Doctor"
            className="profile-picture"
          />
          <p className="doctor-name">
            {doctorProfile.doctorName || "Loading Name..."}
          </p>
        </div>
        <ul className="menu-list">
          {[
            "Dashboard",
            "Edit Profile",
            "Your Schedule",
            "Approve Appointments",  // New Menu Item
            "Accepted Appointments",
            "Add Prescriptions",
          ].map((menu) => (
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
        <div className="Doctor-navbar">
          <ul>
            <li>
              <a href="/doctor-dashboard">Home</a>
            </li>
            <li>
              <span>Welcome {doctorEmail || "Loading..."}</span>
            </li>
            <li>
              <a href="/WelcomePage">Logout</a>
            </li>
          </ul>
        </div>

        {/* Header */}
        <div className="header">
        </div>

        {/* Content */}
        <div className="cards-container">
          {selectedMenu === "Dashboard" && (
            <>
              <div className="card">
                <h2>Appointments</h2>
                <p>2</p>
              </div>
              <div className="card">
                <h2>Total Patients</h2>
                <p>3 patients</p>
              </div>
              <div className="card">
                <h2>Total Slots</h2>
                <p>3 slots</p>
              </div>
            </>
          )}

          {selectedMenu === "Edit Profile" && (
            <EditProfile doctorProfile={doctorProfile} setDoctorProfile={setDoctorProfile} />
          )}

          {selectedMenu === "Your Schedule" && <YourSchedule />}

          {selectedMenu === "Approve Appointments" && <ApproveAppointments />} {/* Approve Appointments here */}

          {selectedMenu === "Accepted Appointments" && <Appointments />} 



          
        </div>
      </div>
    </div>
  );
};

export default DoctorDashboard;