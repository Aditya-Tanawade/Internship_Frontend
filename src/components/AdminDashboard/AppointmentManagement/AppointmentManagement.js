import React, { useState, useEffect } from "react";
import axios from "axios";

const AppointmentManagement = () => {
  const [appointments, setAppointments] = useState([]);

  // Fetch appointments data
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/admin/appointments")
      .then((response) => {
        setAppointments(response.data);
      })
      .catch((error) => {
        console.error("Error fetching appointments:", error);
      });
  }, []);

  const deleteAppointment = (id) => {
    if (window.confirm("Are you sure you want to delete this appointment?")) {
      axios
        .delete(`http://localhost:8080/api/admin/appointment/${id}`)
        .then(() => {
          setAppointments((prev) =>
            prev.filter((appointment) => appointment.id !== id)
          );
        })
        .catch((error) => console.error("Error deleting appointment:", error));
    }
  };

  return (
    <div>
      <h2>Appointment Management</h2>
      <table>
        <thead>
          <tr>
            <th>Doctor</th>
            <th>Patient</th>
            <th>Date</th>
            <th>Time</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {appointments.map((appointment) => (
            <tr key={appointment.id}>
              <td>{appointment.doctorName}</td>
              <td>{appointment.patientName}</td>
              <td>{appointment.date}</td>
              <td>{appointment.time}</td>
              <td>
                <button>Edit</button>
                <button onClick={() => deleteAppointment(appointment.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AppointmentManagement;
