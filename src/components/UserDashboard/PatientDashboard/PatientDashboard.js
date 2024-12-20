import axios from "axios";
import React, { useEffect, useState } from "react";
import AppointmentsList from "../Appointment/AppointmentsList";
import DoctorList from "../DoctorList/DoctorList";
import EditPatientProfile from "../EditProfile/EditPatientProfile";
import "./PatientDashboard.css";
import Chatbot from "../../Chatbot/Chatbot";

const PatientDashboard = () => {
  const [selectedMenu, setSelectedMenu] = useState
  ("Dashboard");
  const [userEmail, setUserEmail] = useState("");
  const [userProfile, setUserProfile] = useState({
    patientName: "",
    mobileNo: "",
    bloodGroup: "",
    gender: "",
    age: "",
    address: "",
  });
  const [isChatbotOpen, setIsChatbotOpen] = useState(false); // State to toggle chatbot visibility

  // Function to fetch user's email
  const fetchUserEmail = () => {
    axios
      .get("http://localhost:8080/api/patient/get-welcome-email")
      .then((response) => {
        setUserEmail(response.data.email || "Unknown Email");
      })
      .catch((error) => {
        console.error("Error fetching email:", error);
        setUserEmail("Error fetching email");
      });
  };

  // Function to fetch user's profile
  const fetchUserProfile = () => {
    axios
      .get("http://localhost:8080/api/patient/profile")
      .then((response) => {
        setUserProfile(response.data);
      })
      .catch((error) => {
        console.error("Error fetching profile:", error);
      });
  };

  // Fetch data on component load
  useEffect(() => {
    fetchUserEmail();
    fetchUserProfile();
  }, []);

  const handleMenuClick = (menu) => {
    setSelectedMenu(menu);
  };

  const toggleChatbot = () => {
    setIsChatbotOpen(!isChatbotOpen);
  };

  return (
    <div className="dashboard-container">
      {/* Sidebar */}
      <div className="sidebar">
        <div className="profile-section">
          <img
            src="assets/img/maleuser.png"
            alt="User"
            className="profile-picture"
          />
          <p className="user-name">
            {userProfile.patientName || "Loading Name..."}
          </p>
        </div>
        <ul className="menu-list">
          {[
            "Dashboard",
            "Edit Profile",
            "My Appointments",
            "Prescriptions",
            "Doctors List",
            "Health Records",
            "Settings",
          ].map((menu) => (
            <li
              key={menu}
              className={
                selectedMenu === menu ? "menu-item active" : "menu-item"
              }
              onClick={() => handleMenuClick(menu)}
            >
              {menu}
            </li>
          ))}
        </ul>
      </div>

      {/* Main Content */}
      <div className="main-content">
        <div className="User-navbar">
          <ul>
            <li>
              <a href="/user-dashboard">Home</a>
            </li>
            <li>
              <span>Welcome {userEmail || "Loading..."}</span>
            </li>
            <li>
              <a href="/WelcomePage">Logout</a>
            </li>
          </ul>
        </div>

        {/* Content */}
        <div className="cards-container">
          {selectedMenu === "Dashboard" && (
            <>
              <div className="card">
                <h2>Upcoming Appointments</h2>
                <p>2</p>
              </div>
              <div className="card">
                <h2>Health Records</h2>
                <p>5 records</p>
              </div>
              <div className="card">
                <h2>Prescriptions</h2>
                <p>8 prescriptions</p>
              </div>
            </>
          )}

          {selectedMenu === "Edit Profile" && (
            <EditPatientProfile
              refreshData={() => {
                fetchUserEmail();
                fetchUserProfile();
              }}
            />
          )}

          {selectedMenu === "Doctors List" && <DoctorList />} {/* Render Doctor List */}

          {selectedMenu === "My Appointments" && <AppointmentsList />} {/* Render Appointments List */}

          {/* Other Menus */}
          {selectedMenu !== "Dashboard" &&
            selectedMenu !== "Edit Profile" &&
            selectedMenu !== "My Appointments" &&
            selectedMenu !== "Doctors List" && (
              <div className="dynamic-content">
                <p>
                  Showing details for: <strong>{selectedMenu}</strong>
                </p>
              </div>
            )}
        </div>
      </div>

      {/* Chatbot Icon */}
      <div
        className="chatbot-icon"
        onClick={toggleChatbot}
        style={{
          position: "fixed",
          bottom: "20px",
          right: "20px",
          cursor: "pointer",
          backgroundColor: "#007bff",
          color: "white",
          borderRadius: "50%",
          width: "60px",
          height: "60px",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          fontSize: "24px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.3)",
        }}
      >
        💬
      </div>

      {/* Chatbot Component */}
      {isChatbotOpen && (
        <div
          style={{
            position: "fixed",
            bottom: "90px", // Adjusted to be above the chatbot open button
            right: "20px",
            zIndex: 1000,
          }}
        >
          <Chatbot />
        </div>
      )}
    </div>
  );
};

export default PatientDashboard;