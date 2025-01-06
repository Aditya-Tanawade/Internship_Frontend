import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";


const EditPatientProfile = () => {
  const { email } = useParams();
  const navigate = useNavigate();
  const [patient, setPatient] = useState({
    patientName: "",
    email: "",
    mobileNo: "",
    bloodGroup: "",
    gender: "",
    age: "",
    address: "",
  });

  const [error, setError] = useState(null);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/patient/get-patient/${email}`)
      .then((response) => setPatient(response.data))
      .catch((error) => setError("Failed to load patient details."));
  }, [email]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setPatient({ ...patient, [name]: value });
  };

  const handleSaveChanges = () => {
    axios
      .put(`http://localhost:8080/api/admin/edit-patient-profile/${email}`, patient)
      .then(() => {
        alert("Patient Profile Updated successfully!");
        navigate("/admin-dashboard");
      })
      .catch(() => setError("Failed to save changes."));
  };

  if (error) {
    return <div className="edit-doctor-error-message">{error}</div>;
  }

  return (
    <div className="edit-doctor-container">
      <h1>
        <center>Edit Patient Profile</center>
      </h1>
      <form className="edit-doctor-profile-form">
        <label>
          Name:
          <input
            type="text"
            name="patientName"
            value={patient.patientName}
            onChange={handleInputChange}
            required
          />
        </label>
        <label>
          Mobile No:
          <input
            type="text"
            name="mobileNo"
            value={patient.mobileNo}
            onChange={handleInputChange}
            required
          />
        </label>
        <label>
          Blood Group:
          <input
            type="text"
            name="bloodGroup"
            value={patient.bloodGroup}
            onChange={handleInputChange}
          />
        </label>
        <label>
          Gender:
          <select
            name="gender"
            value={patient.gender}
            onChange={handleInputChange}
            required
          >
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
          </select>
        </label>
        <label>
          Age:
          <input
            type="number"
            name="age"
            value={patient.age}
            onChange={handleInputChange}
            required
          />
        </label>
        <label>
          Address:
          <textarea
            name="address"
            value={patient.address}
            onChange={handleInputChange}
            required
          ></textarea>
        </label>
        <button type="button" onClick={handleSaveChanges}>
          Save Changes
        </button>
      </form>
    </div>
  );
};

export default EditPatientProfile;
