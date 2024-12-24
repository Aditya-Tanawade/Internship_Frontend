
import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Prescription.css";
import jsPDF from "jspdf";

const Prescription = () => {
  const appointmentId = localStorage.getItem("appointmentId");// Get appointmentId from URL
  const [prescription, setPrescription] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!appointmentId) {
      setError("Appointment ID is missing!");
      return;
    }
    const fetchPrescription = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/prescriptions/${appointmentId}` // Use the appointmentId to fetch prescription
        );
        setPrescription(response.data);
      } catch (err) {
        setError("Error fetching prescription. Please try again later.");
        console.error(err);
      }
    };

    fetchPrescription();
  }, [appointmentId]);

  const handleDownloadPDF = () => {
    const doc = new jsPDF();

    doc.setFontSize(20);
    doc.text("Your Prescription", 90, 10);

    doc.setFontSize(12);
    doc.text(`Doctor: Dr. ${prescription.doctorName}`, 10, 30);
    doc.text(`Specialization: ${prescription.doctorSpeciality}`, 10, 40);
    doc.text(`Charge: ${prescription.doctorCharge}`, 10, 50);
    doc.text(`Email: ${prescription.doctorEmail}`, 10, 60);

    doc.text(`Patient Name: ${prescription.patientName}`, 10, 70);
    doc.text(`Patient Age: ${prescription.patientAge}`, 10, 80);
    doc.text(`Reason: ${prescription.reason}`, 10, 90);
    doc.text(`Advice: ${prescription.advice}`, 10, 100);

    doc.text("Follow the advice properly to ensure a speedy recovery.", 10, 120);
    doc.text("Get Well Soon!", 10, 130);

    doc.save("prescription.pdf");
  };

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!prescription) {
    return <div className="loading-message">Loading prescription...</div>;
  }

  return (
    <div className="prescription-component">
      <div className="prescription-container">
        <header>
          <h1>Your Prescription</h1>
        </header>
        <div className="prescription-card">
          <div className="header-section">
            <h2>E-DOCTOR</h2>
            <hr />
            <div className="doctor-details">
              <div className="doctor-details-column">
                <p>
                  <strong>Doctor Name:</strong> Dr. {prescription.doctorName}
                </p>
                <p>
                  <strong>Specialization:</strong> {prescription.doctorSpeciality}
                </p>
              </div>
              <div className="doctor-details-column">
                <p>
                  <strong>Doctor Charge:</strong> {prescription.doctorCharge}
                </p>
                <p>
                  <strong>Doctor Email:</strong> {prescription.doctorEmail}
                </p>
              </div>
            </div>
          </div>

          <div className="patient-section">
            <p>
              <strong>Patient Name:</strong> {prescription.patientName}
            </p>
            <p>
              <strong>Patient Email:</strong> {prescription.patientEmail}
            </p>
            <p>
              <strong>Patient Age:</strong> {prescription.patientAge}
            </p>
            <p>
              <strong>Patient Gender:</strong> {prescription.patientGender}
            </p>
          </div>

          <div className="details-section">
            <p>
              <strong>Reason:</strong> {prescription.reason}
            </p>
            <p>
              <strong>Advice:</strong> {prescription.advice}
            </p>
          </div>
          <div className="footer-section">
            <p>
              <strong>Note:</strong> Follow the advice properly to ensure a
              speedy recovery.
            </p>
            <p>
              <strong>Get Well Soon!</strong>
            </p>
            <button onClick={handleDownloadPDF} className="download-button">
              Download as PDF
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Prescription;




// import React, { useEffect, useState } from "react";
// import axios from "axios";
// import "./Prescription.css";

// const Prescription = () => {
//   const appointmentId = localStorage.getItem("appointmentId"); // Get the saved appointmentId

//   const [prescription, setPrescription] = useState(null);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     if (!appointmentId) {
//       setError("Appointment ID is missing!");
//       return;
//     }

//     const fetchPrescription = async () => {
//       try {
//         const response = await axios.get(
//           `http://localhost:8080/prescriptions/${appointmentId}` 
//         );
//         setPrescription(response.data);
//       } catch (err) {
//         setError("Error fetching prescription. Please try again later.");
//       }
//     };

//     fetchPrescription();
//   }, [appointmentId]);

//   if (error) {
//     return <div className="error-message">{error}</div>;
//   }

//   if (!prescription) {
//     return <div>Loading prescription...</div>;
//   }

//   return (
//     <div className="prescription-container">
//       <h2>Prescription Details</h2>
//       {/* Render prescription details */}
//       <p>Doctor's Notes: {prescription.doctorNotes}</p>
//       <p>Medication: {prescription.medication}</p>
//       <p>Dosage: {prescription.dosage}</p>
//       <p>Instructions: {prescription.instructions}</p>
//     </div>
//   );
// };

// export default Prescription;

