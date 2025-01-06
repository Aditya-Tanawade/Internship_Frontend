
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

const DoctorLogin = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [passwordError, setPasswordError] = useState(''); // For inline password validation
  const [rememberMe, setRememberMe] = useState(false); // State for "Remember Me"
  const navigate = useNavigate();

  useEffect(() => {
    const bodyElement = document.body;
    bodyElement.style.backgroundImage = "url('assets/img/doctorBgg.png')";
    bodyElement.style.backgroundSize = 'cover';
    bodyElement.style.backgroundRepeat = 'no-repeat';
    bodyElement.style.backgroundPosition = 'center';
    bodyElement.style.height = '100vh';
    bodyElement.style.margin = '0';
    bodyElement.style.padding = '0';
    bodyElement.style.overflow = 'hidden';

    return () => {
      bodyElement.style.backgroundImage = '';
      bodyElement.style.backgroundSize = '';
      bodyElement.style.backgroundRepeat = '';
      bodyElement.style.backgroundPosition = '';
      bodyElement.style.height = '';
      bodyElement.style.margin = '';
      bodyElement.style.padding = '';
      bodyElement.style.overflow = '';
    };
  }, []);

  const validateInputs = () => {
    if (password.length < 6) {
      setPasswordError('Password must be at least 6 characters long');
      return false;
    }
    setPasswordError(''); // Clear password error if validation passes
    return true;
  };

  const handleLogin = async (e) => {
    e.preventDefault();
  
    // Check if the email or password is empty
    if (!email || !password) {
      if (!email) {
        setError('Email cannot be empty.');
      }
      if (!password) {
        setPasswordError('Password cannot be empty.');
      }
      return;
    }
  
    // Reset error messages before making the request
    setError('');
    setPasswordError('');
  
    if (!validateInputs()) {
      return;
    }
  
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        email: email,
        password: password,
      });
  
      if (response.data.status === 'OK') {
        if (response.data.message === 'Doctor login successful') {
          alert('Doctor login successful!');
          navigate('/doctor-dashboard');
        } else {
          setError('Unauthorized role. Only Doctors can log in.');
        }
      } else {
        setError(response.data.message || 'An unexpected error occurred.');
      }
    } catch (err) {
      if (err.response) {
        if (err.response.status === 401) {
          setError('Unauthorized : Invalid email or password.');
        } else {
          setError(err.response.data.message || 'An error occurred.');
        }
      } else {
        setError('An error occurred. Please try again later.');
      }
      console.error(err);
    }
  };
  

  const handleForgotPassword = () => {
    navigate('/forgot-password');
  };

  const handleRegister = () => {
    navigate('/registration');
  };

  return (
    <div className="login-container">
      <h2>Doctor Login</h2>
      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleLogin} className="login-form">
        <div className="form-group">
          <label htmlFor="email">Doctor's Email *</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            placeholder="Enter your email"
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Doctor's Password *</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            placeholder="Enter your password"
          />
          {passwordError && <small className="error-message">{passwordError}</small>}
        </div>

        <div className="remember-forgot-row">
          <div className="remember-me">
            <input
              type="checkbox"
              id="rememberMe"
              checked={rememberMe}
              onChange={() => setRememberMe(!rememberMe)}
            />
            <label htmlFor="rememberMe">Remember Me</label>
          </div>

          <span onClick={handleForgotPassword} className="forgot-password-link">
            Forgot Password?
          </span>
        </div>

        <button type="submit">Login</button>
      </form>

      <div className="register-link-container">
        <p>
          Don't have an account?{' '}
          <span onClick={handleRegister} className="register-link">
            Register here
          </span>
        </p>
      </div>
    </div>
  );
};

export default DoctorLogin;

