import React, { useState, useEffect } from "react";
import axios from "axios";

const PatientManagement = () => {
  const [patients, setPatients] = useState([]);

  // Fetch patients data
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/admin/patients")
      .then((response) => {
        setPatients(response.data);
      })
      .catch((error) => {
        console.error("Error fetching patients:", error);
      });
  }, []);

  const deletePatient = (id) => {
    if (window.confirm("Are you sure you want to delete this patient?")) {
      axios
        .delete(`http://localhost:8080/api/admin/patient/${id}`)
        .then(() => {
          setPatients((prev) => prev.filter((patient) => patient.id !== id));
        })
        .catch((error) => console.error("Error deleting patient:", error));
    }
  };

  return (
    <div>
      <h2>Patient Management</h2>
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Mobile</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {patients.map((patient) => (
            <tr key={patient.id}>
              <td>{patient.name}</td>
              <td>{patient.email}</td>
              <td>{patient.mobile}</td>
              <td>
                <button>Edit</button>
                <button onClick={() => deletePatient(patient.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PatientManagement;
