// import axios from 'axios'; // Import axios for API calls
// import React, { useEffect, useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import './Registration.css';

// const Registration = () => {
//   const [email, setEmail] = useState('');
//   const [name, setName] = useState('');
//   const [password, setPassword] = useState('');
//   const [confirmPassword, setConfirmPassword] = useState('');
//   const [role, setRole] = useState('user'); // Default role as "user"
//   const [errorMessages, setErrorMessages] = useState([]); // Store error messages
//   const [showLoginPrompt, setShowLoginPrompt] = useState(false); // State to control login prompt visibility
//   const [showErrors, setShowErrors] = useState(false); // Control visibility of error messages
//   const navigate = useNavigate();

//   // Set the background image on the body element
//   useEffect(() => {
//     const bodyElement = document.body;
//     bodyElement.style.backgroundImage = "url('assets/img/regist1.png')"; 
//     bodyElement.style.backgroundSize = 'cover';
//     bodyElement.style.backgroundRepeat = 'no-repeat';
//     bodyElement.style.backgroundPosition = 'center';
//     bodyElement.style.height = '100vh';
//     bodyElement.style.margin = '0';
//     bodyElement.style.padding = '0';

//     return () => {
//       bodyElement.style.backgroundImage = '';
//       bodyElement.style.backgroundSize = '';
//       bodyElement.style.backgroundRepeat = '';
//       bodyElement.style.backgroundPosition = '';
//       bodyElement.style.height = '';
//       bodyElement.style.margin = '';
//       bodyElement.style.padding = '';
//     };
//   }, []);

//   useEffect(() => {
//     if (showErrors && errorMessages.length > 0) {
//       const timer = setTimeout(() => {
//         setErrorMessages([]);
//         setShowErrors(false);
//       }, 3000); 

//       return () => clearTimeout(timer);
//     }
//   }, [showErrors, errorMessages]);

//   const handleRegistration = async (e) => {
//     e.preventDefault();

//     if (password !== confirmPassword) {
//       setErrorMessages(["Passwords don't match"]);
//       setShowErrors(true);
//       return;
//     }

//     if (password.length < 6) {
//       setErrorMessages(["Password must be at least 6 characters long"]);
//       setShowErrors(true);
//       return;
//     }

//     try {
//       const response = await axios.post('http://localhost:8080/api/auth/register', {
//         email: email.trim(),
//         username: name.trim(),
//         password,
//         confirmPassword,
//         role: role.toUpperCase()
//       });

//       if (response.data.status === 'OK'){
//         alert('Registration successful!');
//         navigate(role === 'user' ? '/user-login' : '/doctor-login');
//       }
//     } catch (error) {
//       if (error.response && error.response.data) {
//         const errorMessage = error.response.data.message;
//         if (errorMessage === "Username already exists! Please log in.") {
//           setShowLoginPrompt(true);
//         } else {
//           setErrorMessages([errorMessage]);
//           setShowErrors(true);
//         }
//       } else {
//         setErrorMessages(['An unexpected error occurred']);
//         setShowErrors(true);
//       }
//     }
//   };

//   return (
//     <div className="registration-container">
//       <h2>Registration</h2>
//       {showLoginPrompt && (
//         <div className="registration-login-prompt">
//           <p>Email already exists! Please <a href="/doctor-login">Login here</a></p>
//         </div>
//       )}
      
//       {showErrors && errorMessages.length > 0 && (
//         <div className="registration-error-messages">
//           <ul>
//             {errorMessages.map((err, index) => (
//               <li key={index}>{err}</li>
//             ))}
//           </ul>
//         </div>
//       )}

//       <form onSubmit={handleRegistration} className="registration-form">
//         <div className="form-group">
//           <label htmlFor="email">Email</label>
//           <input
//             type="email"
//             id="email"
//             value={email}
//             onChange={(e) => setEmail(e.target.value)}
//             required
//             placeholder="Enter your email"
//           />
//         </div>
//         <div className="form-group">
//           <label htmlFor="name">Username</label>
//           <input
//             type="text"
//             id="name"
//             value={name}
//             onChange={(e) => setName(e.target.value)}
//             required
//             placeholder="Enter your username (3-50 characters)"
//           />
//         </div>
//         <div className="form-group">
//           <label htmlFor="password">Password</label>
//           <input
//             type="password"
//             id="password"
//             value={password}
//             onChange={(e) => setPassword(e.target.value)}
//             required
//             minLength="6"
//             placeholder="Enter your password (min 6 characters)"
//           />
//         </div>
//         <div className="form-group">
//           <label htmlFor="confirmPassword">Confirm Password</label>
//           <input
//             type="password"
//             id="confirmPassword"
//             value={confirmPassword}
//             onChange={(e) => setConfirmPassword(e.target.value)}
//             required
//             minLength="6"
//             placeholder="Confirm your password"
//           />
//         </div>
//         <div className="form-group">
//           <label htmlFor="role">Role</label>
//           <select
//             id="role"
//             value={role}
//             onChange={(e) => setRole(e.target.value)}
//             required
//           >
//             <option value="">Select Role</option>
//             <option value="doctor">DOCTOR</option>
//             <option value="user">USER</option>
//           </select>
//         </div>

//         <button type="submit">Register</button>
//       </form>
//       <div className="registration-login-link">
//         <p>
//           Already have an account?{' '}
//           <a href={role === 'user' ? "/user-login" : "/doctor-login"}>Login here</a>
//         </p>
//       </div>
//     </div>
//   );
// };

// export default Registration;
//till right
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Registration.css';

const Registration = () => {
  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [role, setRole] = useState('user');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [confirmPasswordError, setConfirmPasswordError] = useState('');
  const [setErrorMessages] = useState([]);
  const [showLoginPrompt, setShowLoginPrompt] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const bodyElement = document.body;
    bodyElement.style.backgroundImage = "url('assets/img/regist1.png')";
    bodyElement.style.backgroundSize = 'cover';
    bodyElement.style.backgroundRepeat = 'no-repeat';
    bodyElement.style.backgroundPosition = 'center';
    bodyElement.style.height = '100vh';

    return () => {
      bodyElement.style.backgroundImage = '';
      bodyElement.style.backgroundSize = '';
      bodyElement.style.backgroundRepeat = '';
      bodyElement.style.backgroundPosition = '';
      bodyElement.style.height = '';
    };
  }, []);

  const handleValidation = () => {
    let valid = true;

    // Validate email
    if (!email.trim()) {
      setEmailError('Email is required');
      valid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim())) {
      setEmailError('Invalid email format. Please include "@"');
      valid = false;
    } else {
      setEmailError('');
    }

    // Validate password
    if (password.length < 6) {
      setPasswordError('Password must be at least 6 characters long');
      valid = false;
    }
    // } else if (!/(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/.test(password)) {
    //   setPasswordError(
    //     'Password must include at least one uppercase letter, one lowercase letter, and one number'
    //   );
    //   valid = false;
    // } else {
    //   setPasswordError('');
    // }

    // Validate confirm password
    if (confirmPassword !== password) {
      setConfirmPasswordError("Passwords don't match");
      valid = false;
    } else {
      setConfirmPasswordError('');
    }

    return valid;
  };

  const handleRegistration = async (e) => {
    e.preventDefault();

    if (!handleValidation()) return;

    try {
      const response = await axios.post('http://localhost:8080/api/auth/register', {
        email: email.trim(),
        username: name.trim(),
        password,
        confirmPassword,
        role: role.toUpperCase(),
      });

      if (response.data.status === 'OK') {
        alert('Registration successful!');
        navigate(role === 'user' ? '/user-login' : '/doctor-login');
      }
    } catch (error) {
      if (error.response && error.response.data) {
        const errorMessage = error.response.data.message;
        if (errorMessage === 'Username already exists! Please log in.') {
          setShowLoginPrompt(true);
        } else {
          setErrorMessages([errorMessage]);
        }
      } else {
        setErrorMessages(['An unexpected error occurred']);
      }
    }
  };

  return (
    <div className="registration-container">
      <h2>Registration</h2>
      {showLoginPrompt && (
        <div className="registration-login-prompt">
          <p>
            Email already exists! Please <a href="/doctor-login">Login here</a>
          </p>
        </div>
      )}
      <form onSubmit={handleRegistration} className="registration-form">
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            type="text"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter your email"
          />
          {emailError && <small className="error-message">{emailError}</small>}
        </div>
        <div className="form-group">
          <label htmlFor="name">Username</label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Enter your username (3-50 characters)"
            required
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Enter your password (min 6 characters)"
          />
          {passwordError && <small className="error-message">{passwordError}</small>}
        </div>
        <div className="form-group">
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            placeholder="Confirm your password"
          />
          {confirmPasswordError && (
            <small className="error-message">{confirmPasswordError}</small>
          )}
        </div>
        <div className="form-group">
          <label htmlFor="role">Role</label>
          <select
            id="role"
            value={role}
            onChange={(e) => setRole(e.target.value)}
            required
          >
            <option value="">Select Role</option>
            <option value="doctor">DOCTOR</option>
            <option value="user">USER</option>
          </select>
        </div>
        <button type="submit">Register</button>
      </form>
      <div className="registration-login-link">
        <p>
          Already have an account?{' '}
          <a href={role === 'user' ? '/user-login' : '/doctor-login'}>Login here</a>
        </p>
      </div>
    </div>
  );
};

export default Registration;