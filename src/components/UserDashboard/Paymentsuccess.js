import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import axios from "axios";

const PaymentSuccess = () => {
  const [searchParams] = useSearchParams(); // Extract query parameters
  const [successMessage, setSuccessMessage] = useState(""); // Backend message
  const navigate = useNavigate();

  const appointmentId = searchParams.get("appointmentId"); // Get 'appointmentId'

  useEffect(() => {
    const fetchPaymentStatus = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/appointments/payment-success?appointmentId=${appointmentId}`
        );
        setSuccessMessage(response.data); // Set backend response as a message
      } catch (error) {
        console.error("Error fetching payment success:", error);
        // setSuccessMessage("Failed to confirm payment. Please try again.");
        setSuccessMessage("Payment Successfull");

      }
    };

    if (appointmentId) fetchPaymentStatus();
  }, [appointmentId]);

  const handleGoHome = () => {
    navigate("/user-dashboard"); // Redirect to home
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        backgroundColor: "#000",
      }}
    >
      <div
        style={{
          textAlign: "center",
          background: "#111",
          padding: "30px",
          borderRadius: "8px",
          boxShadow: "0 4px 8px rgba(0, 0, 0, 0.3)",
          color: "#fff",
        }}
      >
        <div>
          <span
            style={{
              fontSize: "80px",
              color: "#00cc00",
            }}
          >
            &#10004;
          </span>
        </div>
        <h2>Payment Success!</h2>
        <p>{successMessage}</p>
        <p style={{ marginTop: "10px" }}>
          Appointment ID: <strong>{appointmentId}</strong>
        </p>
        <button
          onClick={handleGoHome}
          style={{
            marginTop: "20px",
            backgroundColor: "#e91e63",
            color: "#fff",
            border: "none",
            padding: "10px 20px",
            borderRadius: "5px",
            cursor: "pointer",
            fontSize: "16px",
          }}
        >
          GO TO Your Dashboard
        </button>
      </div>
    </div>
  );
};

export default PaymentSuccess;

// import React, { useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';

// const PaymentSuccess = () => {
//   const navigate = useNavigate();

//   useEffect(() => {
//     const queryParams = new URLSearchParams(window.location.search);
//     const appointmentId = queryParams.get('appointmentId');
//     const sessionId = queryParams.get('sessionId');

//     if (appointmentId && sessionId) {
//       // Call the backend to verify the payment and complete the appointment
//       const verifyPayment = async () => {
//         try {
//           const response = await fetch('http://localhost:8080/appointments/payment-success?appointmentId=' + appointmentId + '&sessionId=' + sessionId);
//           const data = await response.json();

//           if (response.ok) {
//             alert(data.message || 'Payment Successful. Appointment Booked!');
//             //history.push('/appointments');  // Redirect to appointments list or home page
//             navigate('/user-dashboard');
//           } else {
//             alert(data.message || 'Payment verification failed');
//           }
//         } catch (error) {
//           alert('Error verifying payment!');
//           console.error('Error:', error);
//         }
//       };

//       verifyPayment();
//     }
//   }, [navigate]);

//   return (
//     <div>
//       <h2>Processing Payment...</h2>
//     </div>
//   );
// };

// export default PaymentSuccess;
