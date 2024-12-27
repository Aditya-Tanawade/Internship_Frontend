import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom"; // Import useNavigate
import axios from "axios";
import styles from './FeedbackPage.module.css'; // Import the CSS module

const FeedbackPage = () => {
  const { appointmentId } = useParams();  // Get appointmentId from the URL
  const navigate = useNavigate();  // Initialize the navigate function

  const [doctorName, setDoctorName] = useState("");
  const [patientName, setPatientName] = useState("");
  const [rating, setRating] = useState(1); // Default rating
  const [comment, setComment] = useState("");
  const [isLoading, setIsLoading] = useState(true); // To track loading state

  // Fetch doctor's name using appointmentId and patient's name using patientId
  useEffect(() => {
    if (isNaN(appointmentId)) {
      alert("Invalid appointment ID");
      return;
    }

    // Fetch the doctor's name
    axios
      .get(`http://localhost:8080/feedback/${appointmentId}`)
      .then((response) => {
        setDoctorName(response.data);
      })
      .catch((error) => {
        console.error("Error fetching doctor name:", error);
      });

    // Fetch the patient's name
    axios
      .get(`http://localhost:8080/feedback/patient-name`)
      .then((response) => {
        setPatientName(response.data);
      })
      .catch((error) => {
        console.error("Error fetching patient name:", error);
      })
      .finally(() => {
        setIsLoading(false); // Set loading to false once data is fetched
      });
  }, [appointmentId]);

  // Handle feedback submission
  const handleSubmit = (e) => {
    e.preventDefault();

    const feedbackData = {
      patientName,
      doctorName,
      rating,
      comment,
    };

    axios
      .post("http://localhost:8080/feedback/add", feedbackData)
      .then((response) => {
        alert("Feedback submitted successfully!");
        navigate('/user-dashboard');  // Navigate to the user dashboard after successful submission
      })
      .catch((error) => {
        console.error("Error submitting feedback:", error);
        alert("Failed to submit feedback.");
      });
  };

  // Loading state while waiting for the API responses
  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div className={styles['feedback-page']}>
      <h2>Submit Your Feedback</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Doctor's Name</label>
          <input type="text" value={doctorName} readOnly />
        </div>
        <div>
          <label>Patient's Name</label>
          <input type="text" value={patientName} readOnly />
        </div>

        <div>
          <label>Rating</label>
          <div className={styles.stars}>
            {[1, 2, 3, 4, 5].map((star) => (
              <span
                key={star}
                className={`${styles.star} ${rating >= star ? styles.filled : ''}`}
                onClick={() => setRating(star)}
              >
                ★
              </span>
            ))}
          </div>
        </div>

        <div>
          <label>Comment</label>
          <textarea
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            placeholder="Add a comment"
          />
        </div>
        <button type="submit">Submit Feedback</button>
      </form>
    </div>
  );
};

export default FeedbackPage;
