// import React, { useEffect, useState } from "react";
// import styles from "./AppointmentsList.module.css";

// const Appointments = () => {
//   const [appointments, setAppointments] = useState([]);

//   // Fetch appointments from the backend
//   useEffect(() => {
//     fetch("http://localhost:8080/api/patient/appointments")
//       .then((response) => {
//         if (!response.ok) {
//           throw new Error("Failed to fetch appointments");
//         }
//         return response.json();
//       })
//       .then((data) => setAppointments(data))
//       .catch((error) => console.error(error.message));
//   }, []);

//   const cancelAppointment = (appointmentId) => {
//     fetch(`http://localhost:8080/api/patient/appointments/${appointmentId}`, {
//       method: "DELETE",
//     })
//       .then((response) => {
//         if (response.ok) {
//           alert("Appointment cancelled successfully.");
//           setAppointments((prevAppointments) =>
//             prevAppointments.filter((appointment) => appointment.id !== appointmentId)
//           );
//         } else {
//           alert("Failed to cancel appointment.");
//         }
//       })
//       .catch((error) => console.error(error.message));
//   };

//   const downloadPrescription = (appointmentId) => {
//     // Logic to download the prescription
//     alert(`Downloading prescription for appointment ID: ${appointmentId}`);
//   };

//   const giveFeedback = (appointmentId) => {
//     // Logic to redirect to feedback page or open a modal
//     alert(`Giving feedback for appointment ID: ${appointmentId}`);
//   };

//   return (
//     <div className={styles.appointmentsContainer}>
//       <h2>My Appointments</h2>
//       {appointments.length > 0 ? (
//         <table className={styles.appointmentsTable}>
//           <thead>
//             <tr>
//               <th>Doctor Email</th>
//               <th>Appointment Date</th>
//               <th>Reason</th>
//               <th>Status</th>
//               <th>Payment Mode</th>
//               <th>Actions</th>
//             </tr>
//           </thead>
//           <tbody>
//             {appointments.map((appointment) => (
//               <tr key={appointment.id}>
//                 <td>{appointment.doctorEmail}</td>
//                 <td>{appointment.appointmentDate}</td>
//                 <td>{appointment.reason}</td>
//                 <td>
//                   <span
//                     className={`${styles.statusBox} ${
//                       styles[appointment.status || "PENDING"]
//                     }`}
//                   >
//                     {appointment.status || "PENDING"}
//                   </span>
//                 </td>
//                 <td>
//                   {appointment.paymentmode === "ONLINE_PAY" ? "Online Pay" : "Cash"}
//                 </td>
//                 <td>
//                   {appointment.status !== "COMPLETED" && (
//                     <button
//                       onClick={() => cancelAppointment(appointment.id)}
//                       className={styles.cancelButton}
//                     >
//                       Cancel
//                     </button>
//                   )}
//                   {appointment.status === "COMPLETED" && (
//                     <>
//                       <button
//                         onClick={() => downloadPrescription(appointment.id)}
//                         className={styles.downloadButton}
//                       >
//                         Prescription
//                       </button>
//                       <button
//                         onClick={() => giveFeedback(appointment.id)}
//                         className={styles.feedbackButton}
//                       >
//                         Feedback
//                       </button>
//                     </>
//                   )}
//                 </td>
//               </tr>
//             ))}
//           </tbody>
//         </table>
//       ) : (
//         <p className={styles.emptyMessage}>No appointments found.</p>
//       )}
//     </div>
//   );
// };

// export default Appointments;




// import { useNavigate } from "react-router-dom";
// import React, { useEffect, useState } from "react";

// import styles from "./AppointmentsList.module.css";


// const Appointments = () => {
//   const [appointments, setAppointments] = useState([]);
//   const navigate = useNavigate(); 
  
  
//     useEffect(() => {
//     fetch("http://localhost:8080/api/patient/appointments")
//       .then((response) => {
//         if (!response.ok) {
//           throw new Error("Failed to fetch appointments");
//         }
//         return response.json();
//       })
//       .then((data) => setAppointments(data))
//       .catch((error) => console.error(error.message));
//   }, []);// Use React Router's navigate hook

//   const downloadPrescription = (appointmentId) => {
//     navigate(`/prescriptions/${appointmentId}`);
//   };

//   const giveFeedback = (appointmentId) => {
//     navigate(`/feedback`);
//   };


//   const cancelAppointment = (appointmentId) => {
//         fetch(`http://localhost:8080/api/patient/appointments/${appointmentId}`, {
//           method: "DELETE",
//         })
//           .then((response) => {
//             if (response.ok) {
//               alert("Appointment cancelled successfully.");
//               setAppointments((prevAppointments) =>
//                 prevAppointments.filter((appointment) => appointment.id !== appointmentId)
//               );
//             } else {
//               alert("Failed to cancel appointment.");
//             }
//           })
//           .catch((error) => console.error(error.message));
//       };

//   return (
//     <div className={styles.appointmentsContainer}>
//       <h2>My Appointments</h2>
//       {appointments.length > 0 ? (
//         <table className={styles.appointmentsTable}>
//           <thead>
//             <tr>
//               <th>Doctor Email</th>
//               <th>Appointment Date</th>
//               <th>Reason</th>
//               <th>Status</th>
//               <th>Payment Mode</th>
//               <th>Actions</th>
//             </tr>
//           </thead>
//           <tbody>
//             {appointments.map((appointment) => (
//               <tr key={appointment.id}>
//                 <td>{appointment.doctorEmail}</td>
//                 <td>{appointment.appointmentDate}</td>
//                 <td>{appointment.reason}</td>
//                 <td>
//                   <span
//                     className={`${styles.statusBox} ${
//                       styles[appointment.status || "PENDING"]
//                     }`}
//                   >
//                     {appointment.status || "PENDING"}
//                   </span>
//                 </td>
//                 <td>
//                   {appointment.paymentmode === "ONLINE_PAY" ? "Online Pay" : "Cash"}
//                 </td>
//                 <td>
//                   {appointment.status !== "COMPLETED" && (
//                     <button
//                       onClick={() => cancelAppointment(appointment.id)}
//                       className={styles.cancelButton}
//                     >
//                       Cancel
//                     </button>
//                   )}
//                   {appointment.status === "COMPLETED" && (
//                     <>
//                       <button
//                         onClick={() => downloadPrescription(appointment.id)}
//                         className={styles.downloadButton}
//                       >
//                         Prescription
//                       </button>
//                       <button
//                         onClick={() => giveFeedback(appointment.id)}
//                         className={styles.feedbackButton}
//                       >
//                         Feedback
//                       </button>
//                     </>
//                   )}
//                 </td>
//               </tr>
//             ))}
//           </tbody>
//         </table>
//       ) : (
//         <p className={styles.emptyMessage}>No appointments found.</p>
//       )}
//     </div>
//   );
// };

// export default Appointments;



import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./AppointmentsList.module.css";

const Appointments = () => {
  const [appointments, setAppointments] = useState([]);
  const navigate = useNavigate();

  // Fetch appointments
  useEffect(() => {
    fetch("http://localhost:8080/api/patient/appointments")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch appointments");
        }
        return response.json();
      })
      .then((data) => setAppointments(data))
      .catch((error) => console.error(error.message));
  }, []);

  // Function to navigate to prescription page
  const downloadPrescription = (appointmentId) => {
    navigate(`/prescriptions/${appointmentId}`);
  };

  // Function to navigate to feedback page
  const giveFeedback = (appointmentId) => {
    navigate(`/feedback`);
  };

  // Function to cancel an appointment
  const cancelAppointment = (appointmentId) => {
    fetch(`http://localhost:8080/api/patient/appointments/${appointmentId}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (response.ok) {
          alert("Appointment cancelled successfully.");
          setAppointments((prevAppointments) =>
            prevAppointments.filter((appointment) => appointment.id !== appointmentId)
          );
        } else {
          alert("Failed to cancel appointment.");
        }
      })
      .catch((error) => console.error(error.message));
  };

  return (
    <div className={styles.appointmentsContainer}>
      <h2>My Appointments</h2>
      {appointments.length > 0 ? (
        <table className={styles.appointmentsTable}>
          <thead>
            <tr>
              <th>Doctor Email</th>
              <th>Appointment Date</th>
              <th>Reason</th>
              <th>Status</th>
              <th>Payment Mode</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((appointment) => (
              <tr key={appointment.id}>
                <td>{appointment.doctorEmail}</td>
                <td>{appointment.appointmentDate}</td>
                <td>{appointment.reason}</td>
                <td>
                  <span
                    className={`${styles.statusBox} ${
                      styles[appointment.status || "PENDING"]
                    }`}
                  >
                    {appointment.status || "PENDING"}
                  </span>
                </td>
                <td>
                  {appointment.paymentmode === "ONLINE_PAY" ? "Online Pay" : "Cash"}
                </td>
                <td>
                  {appointment.status !== "COMPLETED" && (
                    <button
                      onClick={() => cancelAppointment(appointment.id)}
                      className={styles.cancelButton}
                    >
                      Cancel
                    </button>
                  )}
                  {appointment.status === "COMPLETED" && (
                      <button
                        onClick={() => downloadPrescription(appointment.id)}
                        className={styles.downloadButton}
                      >
                       Download 
                      Prescription
                      </button>
                     
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p className={styles.emptyMessage}>No appointments found.</p>
      )}
    </div>
  );
};

export default Appointments;
