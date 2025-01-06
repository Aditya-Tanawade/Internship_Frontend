import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";


const UpdateDoctor = () => {
  const [doctors, setDoctors] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/doctor/get-all-doctors")
      .then((response) => {
        console.log("Doctor data:", response.data);
        const fetchedDoctors = response.data || [];
        setDoctors(fetchedDoctors);
      })
      .catch((error) => {
        console.error("Error fetching doctors:", error);
        setError("Failed to fetch doctor list.");
      });
  }, []);

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (doctors.length === 0) {
    return <div className="loading">Loading doctor list...</div>;
  }

  return (
    <div className="doctor-list-container">
      <h1>Doctor List</h1>
      <div className="doctor-cards">
        {doctors.map((doctor, index) => (
          <div className="doctor-card" key={index}>
            <div className="doctor-image-container">
              <img
                src={`assets/img/${doctor.gender === "Female" ? "femaledoctor.png" : "maledoctor.png"}`}
                alt="Doctor"
                className="doctor-image"
              />
            </div>
            <div className="doctor-details">
              

              <p><strong>Name:</strong> {doctor.doctorName || "Profile Not Updated"}</p>
              <p><strong>Email:</strong> {doctor.email}</p>
              <p><strong>Speciality:</strong> {doctor.speciality || "Not specified"}</p>
              <p><strong>Location:</strong> {doctor.location || "Not specified"}</p>
              <p><strong>Gender:</strong> {doctor.gender || "Not specified"}</p> {/* Add this line */}
              <p><strong>Mobile:</strong> {doctor.mobileNo || "Not provided"}</p>
              <p><strong>Hospital:</strong> {doctor.hospitalName || "Not specified"}</p>
              <p><strong>Charge per Visit:</strong> ₹{doctor.chargedPerVisit || 0}</p>

              <button
                className="update-doctor-button"
                onClick={() => navigate(`/edit-doctor/${doctor.email}`)}
              >
                Update Doctor
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default UpdateDoctor;
