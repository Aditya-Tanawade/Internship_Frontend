import React, { useState, useEffect } from "react";
import axios from "axios";

const DoctorManagement = () => {
  const [doctors, setDoctors] = useState([]);

  // Fetch doctors data
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/admin/doctors")
      .then((response) => {
        setDoctors(response.data);
      })
      .catch((error) => {
        console.error("Error fetching doctors:", error);
      });
  }, []);

  const deleteDoctor = (id) => {
    if (window.confirm("Are you sure you want to delete this doctor?")) {
      axios
        .delete(`http://localhost:8080/api/admin/doctor/${id}`)
        .then(() => {
          setDoctors((prev) => prev.filter((doctor) => doctor.id !== id));
        })
        .catch((error) => console.error("Error deleting doctor:", error));
    }
  };

  return (
    <div>
      <h2>Doctor Management</h2>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Specialty</th>
            <th>Mobile</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {doctors.map((doctor) => (
            <tr key={doctor.id}>
              <td>{doctor.name}</td>
              <td>{doctor.specialty}</td>
              <td>{doctor.mobile}</td>
              <td>
                <button>Edit</button>
                <button onClick={() => deleteDoctor(doctor.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DoctorManagement;
