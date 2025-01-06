import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import "./EditPatientProfile.css";

const EditDoctorProfile = () => {
  const { email } = useParams();
  const navigate = useNavigate();
  const [doctor, setDoctor] = useState({
    doctorName: "",
    email: "",
    speciality: "",
    location: "",
    gender: "",  //add gender
    mobileNo: "",
    hospitalName: "",
    chargedPerVisit: 0,
  });

  const [error, setError] = useState(null);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/admin/get-doctor/${email}`)
      .then((response) => setDoctor(response.data))
      .catch((error) => setError("Failed to load doctor details."));
  }, [email]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setDoctor({ ...doctor, [name]: value });
  };

  const handleSaveChanges = () => {
    axios
      .put(`http://localhost:8080/api/admin/edit-doctor-profile/${email}`, doctor)
      .then(() => {
        alert("Doctor Profile Updated successfully!");
        navigate("/admin-dashboard");
      })
      .catch(() => setError("Failed to save changes."));
  };

  if (error) {
    return <div className="edit-doctor-error-message">{error}</div>;
  }

  return (
    <div className="edit-doctor-container">
      <h1><center>Edit Doctor Profile</center></h1>
      <form className="edit-doctor-profile-form">
        <label>
          Name:
          <input
            type="text"
            name="doctorName"
            value={doctor.doctorName}
            onChange={handleInputChange}
          />
        </label>
        <label>
          Speciality:
          <input
            type="text"
            name="speciality"
            value={doctor.speciality}
            onChange={handleInputChange}
          />
        </label>
        <label>
          Location:
          <input
            type="text"
            name="location"
            value={doctor.location}
            onChange={handleInputChange}
          />
        </label>
        <label>
          Gender:
          <select
            name="gender"
            value={doctor.gender}
            onChange={handleInputChange}
          >
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>
        </label>
        <label>
          Mobile No:
          <input
            type="text"
            name="mobileNo"
            value={doctor.mobileNo}
            onChange={handleInputChange}
          />
        </label>
        <label>
          Hospital Name:
          <input
            type="text"
            name="hospitalName"
            value={doctor.hospitalName}
            onChange={handleInputChange}
          />
        </label>
        <label>
          Charge per Visit (₹):
          <input
            type="number"
            name="chargedPerVisit"
            value={doctor.chargedPerVisit}
            onChange={handleInputChange}
          />
        </label>
        <button type="button" onClick={handleSaveChanges}>
          Save Changes
        </button>
      </form>
    </div>
  );
};

export default EditDoctorProfile;
