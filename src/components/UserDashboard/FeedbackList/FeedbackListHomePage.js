import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './FeedbackListHomePage.css';

const FeedbackListHomePage = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch all feedbacks when the component mounts
    axios
      .get("http://localhost:8080/feedback/get-all-feedbacks")
      .then((response) => {
        setFeedbacks(response.data || []);
      })
      .catch((error) => {
        setError("Failed to fetch feedbacks.");
      });
  }, []);

  // Handle like button click
  const handleLikeClick = (feedbackId) => {
    axios
      .post(`http://localhost:8080/feedback/like/${feedbackId}`)
      .then((response) => {
        // Update the feedback list with the new like count
        const updatedFeedbacks = feedbacks.map((feedback) =>
          feedback.id === feedbackId ? { ...feedback, likes: feedback.likes + 1 } : feedback
        );
        setFeedbacks(updatedFeedbacks);
      })
      .catch((error) => {
        console.error("Error liking feedback:", error);
      });
  };

  // Generate stars based on rating
  const renderStars = (rating) => {
    let stars = [];
    for (let i = 1; i <= 5; i++) {
      stars.push(
        <span key={i} className={`star ${i <= rating ? 'filled' : ''}`}>
          ★
        </span>
      );
    }
    return stars;
  };

  return (
    <div className="feedback-list-container">
      {/* Header Section */}
      <div className="feedback-header">
        <h2>User Feedback</h2>
        <p>Here you can see all the feedback provided by users</p>
      </div>

      {error && <p className="error-message">{error}</p>}

      {/* Feedback Cards */}
      <div className="feedback-list">
        {feedbacks.length > 0 ? (
          feedbacks.map((feedback, index) => (
            <div className="feedback-item" key={index}>
              <div className="feedback-item-header">
                <p><strong>Feedback From </strong> {feedback.patientName}</p>
                <p><strong>Doctor:</strong> {feedback.doctorName}</p>
              </div>
              <div className="feedback-rating">
                <strong>Rating:</strong>
                <div className="stars">{renderStars(feedback.rating)}</div>
              </div>
              <div className="feedback-comment">
                <p><strong>Comment:</strong></p>
                <p>{feedback.comment}</p>
              </div>
              <div className="feedback-footer">
                <button 
                  className="like-button" 
                  onClick={() => handleLikeClick(feedback.id)}>
                    Like ({feedback.likes})
                </button>
              </div>
            </div>
          ))
        ) : (
          <p>No feedback available.</p>
        )}
      </div>
    </div>
  );
};

export default FeedbackListHomePage;
