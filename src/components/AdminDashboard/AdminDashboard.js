

import React, { useState, useEffect } from "react";
import axios from "axios";
import DoctorList from "./DoctorList";
import PatientList from "./PatientList";
import UpdatePateint from "./UpdatePatient";
import "./AdminDashboard.css";
import UpdateDoctor from "./UpdateDoctor";

const AdminDashboard = () => {
  const [selectedMenu, setSelectedMenu] = useState("Dashboard");
  const [selectedSubMenu, setSelectedSubMenu] = useState("");
  const [adminEmail, setAdminEmail] = useState("");
  const [doctorsCount, setDoctorsCount] = useState(0);
  const [patientsCount, setPatientsCount] = useState(0);

  // New state for adding doctor and patient
  const [newDoctorEmail, setNewDoctorEmail] = useState("");
  const [newDoctorName, setNewDoctorName] = useState("");
  const [speciality, setSpeciality] = useState("");
  const [location, setLocation] = useState("");
  const [mobileNo, setMobileNo] = useState("");
  const [hospitalName, setHospitalName] = useState("");
  const [chargedPerVisit, setChargedPerVisit] = useState("");

  // New patient state variables
  const [newPatientName, setNewPatientName] = useState("");
  const [newPatientMobileNo, setNewPatientMobileNo] = useState("");
  const [newPatientEmail, setNewPatientEmail] = useState("");
  const [bloodGroup, setBloodGroup] = useState("");
  const [gender, setGender] = useState("");
  const [age, setAge] = useState("");
  const [address, setAddress] = useState("");

  const defaultPassword = "defaultPassword123"; // Default password for new users

  // Fetch admin email
  const fetchAdminEmail = () => {
    axios
      .get("http://localhost:8080/api/admin/get-welcome-email")
      .then((response) => {
        setAdminEmail(response.data.email || "Unknown Email");
      })
      .catch((error) => {
        console.error("Error fetching email:", error);
        setAdminEmail("Error fetching email");
      });
  };

  // Fetch total doctors count
  const fetchDoctorsCount = () => {
    axios
      .get("http://localhost:8080/api/admin/doctors")
      .then((response) => {
        setDoctorsCount(response.data || 0);
      })
      .catch((error) => {
        console.error("Error fetching doctors count:", error);
      });
  };

  // Fetch total patients count
  const fetchPatientsCount = () => {
    axios
      .get("http://localhost:8080/api/admin/patients")
      .then((response) => {
        setPatientsCount(response.data || 0);
      })
      .catch((error) => {
        console.error("Error fetching patients count:", error);
      });
  };

  // Handle adding doctor
  const handleAddDoctor = () => {
    const newDoctor = {
      email: newDoctorEmail,
      doctorName: newDoctorName,
      speciality: speciality,
      location: location,
      mobileNo: mobileNo,
      hospitalName: hospitalName,
      chargedPerVisit: chargedPerVisit,
      gender: gender,
      password: defaultPassword,
      role: "DOCTOR",
    };

    axios
      .post("http://localhost:8080/api/admin/add-doctor", newDoctor)
      .then((response) => {
        alert("Doctor added successfully");
        fetchDoctorsCount(); // Refresh the doctor count
      })
      .catch((error) => {
        console.error("Error adding doctor:", error);
        alert("Error adding doctor");
      });
  };

  // Handle adding patient
  const handleAddPatient = () => {
    const newPatient = {
      patientName: newPatientName,
      mobileNo: newPatientMobileNo,
      email: newPatientEmail,
      bloodGroup: bloodGroup,
      gender: gender,
      age: age,
      address: address,
      password: defaultPassword,
      role: "USER",
    };

    axios
      .post("http://localhost:8080/api/admin/add-patient", newPatient)
      .then((response) => {
        alert("Patient added successfully");
        fetchPatientsCount(); // Refresh the patient count
      })
      .catch((error) => {
        console.error("Error adding patient:", error);
        alert("Error adding patient");
      });
  };

  // Initial data fetch
  useEffect(() => {
    fetchAdminEmail();
    fetchDoctorsCount();
    fetchPatientsCount();
  }, []);

  const handleMenuClick = (menu) => {
    setSelectedMenu(menu);
    setSelectedSubMenu(""); // Reset submenu when switching menus
  };

  return (
    <div className="dashboard-container">
      <div className="sidebar">
        <div className="profile-section">
          <img src="assets/img/admin.png" alt="Admin" className="profile-picture" />
          <p className="admin-email">{adminEmail || "Loading Email..."}</p>
        </div>
        <ul className="menu-list">
          {/* {["Dashboard", "Manage Doctors", "Add Doctor", "Manage Patients", "Add Patient"].map((menu) => ( */}
              {["Dashboard","Patients","Doctors", "Add Doctors", "Manage Doctors", "Add Patients", "Manage Patients"].map((menu) => (
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

      <div className="main-content">
        <div className="Admin-navbar">
          <ul>
            <li><a href="/admin-dashboard">Home</a></li>
            <li><span>Welcome {adminEmail || "Loading..."}</span></li>
            <li><a href="/WelcomePage">Logout</a></li>
          </ul>
        </div>

        <div className="cards-container">
          {selectedMenu === "Dashboard" && (
            <>
              <div className="card">
                <h2>Total Doctors</h2>
                <p>{doctorsCount}</p>
              </div>
              <div className="card">
                <h2>Total Patients</h2>
                <p>{patientsCount}</p>
              </div>
            </>
          )}

          {selectedMenu === "Manage Doctors" && (
            <>
              {/* <h2>Doctor List</h2> */}
              <DoctorList />
            </>
          )}

         

{selectedMenu === "Add Doctors" && (
  <>
    <div className="form-container">
      <form onSubmit={handleAddDoctor} className="form">
        <div className="form-group">
          <label htmlFor="doctorEmail">Doctor's Email</label>
          <input
            type="email"
            id="doctorEmail"
            placeholder="Enter doctor's email"
            value={newDoctorEmail}
            onChange={(e) => setNewDoctorEmail(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="doctorName">Doctor's Name</label>
          <input
            type="text"
            id="doctorName"
            placeholder="Enter doctor's name"
            value={newDoctorName}
            onChange={(e) => setNewDoctorName(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="speciality">Speciality</label>
          <input
            type="text"
            id="speciality"
            placeholder="Enter speciality"
            value={speciality}
            onChange={(e) => setSpeciality(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="location">Location</label>
          <input
            type="text"
            id="location"
            placeholder="Enter location"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="mobileNo">Mobile Number</label>
          <input
            type="text"
            id="mobileNo"
            placeholder="Enter mobile number"
            value={mobileNo}
            onChange={(e) => setMobileNo(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="hospitalName">Hospital Name</label>
          <input
            type="text"
            id="hospitalName"
            placeholder="Enter hospital name"
            value={hospitalName}
            onChange={(e) => setHospitalName(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="chargedPerVisit">Charged Per Visit</label>
          <input
            type="number"
            id="chargedPerVisit"
            placeholder="Enter charged per visit"
            value={chargedPerVisit}
            onChange={(e) => setChargedPerVisit(e.target.value)}
          />
        </div>

        {/* New Gender Field */}
        <div className="form-group">
          <label htmlFor="doctorGender">Gender</label>
          <select
            id="doctorGender"
            value={gender}
            onChange={(e) => setGender(e.target.value)}
            
          >
            <option value="" disabled>
             Select Gender
            </option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            
          </select>
        </div>

        <button type="submit">Add Doctor</button>
      </form>
    </div>
  </>
)}


          {selectedMenu === "Manage Patients" && (
            <>
            
              <PatientList />
            </>
          )}
{/* add */}

{selectedMenu === "Patients" && (
            <>
            
              <UpdatePateint/>
            </>
          )}

{selectedMenu === "Doctors" && (
            <>
            
              <UpdateDoctor/>
            </>
          )}
{/* add */}
{selectedMenu === "Add Patients" && (
  <>
    <div className="form-container">
      <form onSubmit={handleAddPatient} className="form">
        <div className="form-group">
          <label htmlFor="patientEmail">Patient's Email</label>
          <input
            type="email"
            id="patientEmail"
            placeholder="Enter patient's email"
            value={newPatientEmail}
            onChange={(e) => setNewPatientEmail(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="patientName">Patient's Name</label>
          <input
            type="text"
            id="patientName"
            placeholder="Enter patient's name"
            value={newPatientName}
            onChange={(e) => setNewPatientName(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="patientMobileNo">Mobile Number</label>
          <input
            type="text"
            id="patientMobileNo"
            placeholder="Enter patient mobile number"
            value={newPatientMobileNo}
            onChange={(e) => setNewPatientMobileNo(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="bloodGroup">Blood Group</label>
          <input
            type="text"
            id="bloodGroup"
            placeholder="Enter blood group"
            value={bloodGroup}
            onChange={(e) => setBloodGroup(e.target.value)}
          />
        </div>

       
         <div className="form-group">
          <label htmlFor="doctorGender">Gender</label>
          <select
            id="doctorGender"
            value={gender}
            onChange={(e) => setGender(e.target.value)}
            r
          >
            <option value="" disabled>
             Select Gender
            </option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
            
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="age">Age</label>
          <input
            type="number"
            id="age"
            placeholder="Enter age"
            value={age}
            onChange={(e) => setAge(e.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="address">Address</label>
          <input
            type="text"
            id="address"
            placeholder="Enter address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
          />
        </div>

        <button type="submit">Add Patient</button>
      </form>
    </div>
  </>
)}



        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;

