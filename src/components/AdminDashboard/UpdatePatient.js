import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./PatientList.css";

const  UpdatePatient = () => {
  const [patients, setPatients] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/patient/get-all-patients")
      .then((response) => {
        console.log("Patient data:", response.data);
        const fetchedPatients = response.data || [];
        setPatients(fetchedPatients);
      })
      .catch((error) => {
        console.error("Error fetching patients:", error);
        setError("Failed to fetch patient list.");
      });
  }, []);




  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (patients.length === 0) {
    return <div className="loading">Loading patient list...</div>;
  }



return (
  <div className="patient-list-container">
    <h1>Patient List</h1>
    <div className="patient-cards">
      {patients.map((patient, index) => (
        <div className="patient-card" key={index}>
          <div className="patient-image-container">
            <img
              src={`assets/img/${patient.gender === "Female" ? "femaleuser.png" : "maleuser.png"}`}
              alt="Patient"
              className="patient-image"
            />
          </div>
          <div className="patient-details">
            <p><strong>Name:</strong> {patient.patientName || "Profile Not Updated"}</p>
            <p><strong>Email:</strong> {patient.email}</p>
            <p><strong>Mobile:</strong> {patient.mobileNo || "Not provided"}</p>
            <p><strong>Blood Group:</strong> {patient.bloodGroup || "Not specified"}</p>
            <p><strong>Gender:</strong> {patient.gender || "Not specified"}</p>
            <p><strong>Age:</strong> {patient.age || "Not specified"}</p>
            <p><strong>Address:</strong> {patient.address || "Not provided"}</p>

            <button
              // className="delete-patient-button"
               className="update-doctor-button"
              onClick={() => navigate(`/edit-patient/${patient.email}`)}
            >
              Update Patient
            </button>
          </div>
        </div>
      ))}
    </div>
  </div>
);

};

export default UpdatePatient;
