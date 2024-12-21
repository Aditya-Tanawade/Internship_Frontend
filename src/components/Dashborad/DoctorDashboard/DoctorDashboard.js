// src/components/DoctorDashboard.js
// import React, { useState, useEffect } from "react";
// import axios from "axios";
// import "./DoctorDashboard.css";
// import EditProfile from "../EditDoctorProfile/EditProfile";
// import YourSchedule from "../UpdateAvialblity/YourSchedule"; 
// import ApproveAppointments from "../ApproveAppoinments/ApproveAppointments"; // Import the new component
// import Appointments from "../ApproveAppoinments/Appointments"; // Import the Appointments component



// const DoctorDashboard = () => {
//   const [selectedMenu, setSelectedMenu] = useState("Dashboard");
//   const [doctorEmail, setDoctorEmail] = useState("");
//   const [doctorProfile, setDoctorProfile] = useState({
//     doctorName: "",
//     speciality: "",
//     location: "",
//     mobileNo: "",
//     hospitalName: "",
//     chargedPerVisit: "",
//   });

//   // Fetch doctor's email
//   useEffect(() => {
//     axios
//       .get("http://localhost:8080/api/doctor/get-welcome-email")
//       .then((response) => {
//         setDoctorEmail(response.data.email || "Unknown Email");
//       })
//       .catch((error) => {
//         console.error("Error fetching email:", error);
//         setDoctorEmail("Error fetching email");
//       });
//   }, []);

//   // Fetch doctor's profile
//   useEffect(() => {
//     axios
//       .get("http://localhost:8080/api/doctor/profile")
//       .then((response) => {
//         setDoctorProfile(response.data); // Populate doctorProfile with backend data
//       })
//       .catch((error) => {
//         console.error("Error fetching profile:", error);
//       });
//   }, []);

//   const handleMenuClick = (menu) => {
//     setSelectedMenu(menu);
//   };

//   return (
//     <div className="dashboard-container">
//       {/* Sidebar */}
//       <div className="sidebar">
//         <div className="profile-section">
//           <img
//             src="assets/img/maledoctor.png"
//             alt="Doctor"
//             className="profile-picture"
//           />
//           <p className="doctor-name">
//             {doctorProfile.doctorName || "Loading Name..."}
//           </p>
//         </div>
//         <ul className="menu-list">
//           {[
//             "Dashboard",
//             "Edit Profile",
//             "Your Schedule",
//             "Approve Appointments",  // New Menu Item
//             "Accepted Appointments",
//             "Add Prescriptions",
//           ].map((menu) => (
//             <li
//               key={menu}
//               className={selectedMenu === menu ? "menu-item active" : "menu-item"}
//               onClick={() => handleMenuClick(menu)}
//             >
//               {menu}
//             </li>
//           ))}
//         </ul>
//       </div>

//       {/* Main Content */}
//       <div className="main-content">
//         <div className="Doctor-navbar">
//           <ul>
//             <li>
//               <a href="/doctor-dashboard">Home</a>
//             </li>
//             <li>
//               <span>Welcome {doctorEmail || "Loading..."}</span>
//             </li>
//             <li>
//               <a href="/WelcomePage">Logout</a>
//             </li>
//           </ul>
//         </div>

//         {/* Header */}
//         <div className="header">
//         </div>

//         {/* Content */}
//         <div className="cards-container">
//           {selectedMenu === "Dashboard" && (
//             <>
//               <div className="card">
//                 <h2>Appointments</h2>
//                 <p>2</p>
//               </div>
//               <div className="card">
//                 <h2>Total Patients</h2>
//                 <p>3 patients</p>
//               </div>
//               <div className="card">
//                 <h2>Total Slots</h2>
//                 <p>3 slots</p>
//               </div>
//             </>
//           )}

//           {selectedMenu === "Edit Profile" && (
//             <EditProfile doctorProfile={doctorProfile} setDoctorProfile={setDoctorProfile} />
//           )}

//           {selectedMenu === "Your Schedule" && <YourSchedule />}

//           {selectedMenu === "Approve Appointments" && <ApproveAppointments />} {/* Approve Appointments here */}

//           {selectedMenu === "Accepted Appointments" && <Appointments />} 



          
//         </div>
//       </div>
//     </div>
//   );
// };

// export default DoctorDashboard;
//till right
import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import "./DoctorDashboard.css";
import EditProfile from "../EditDoctorProfile/EditProfile";
import YourSchedule from "../UpdateAvialblity/YourSchedule";
import ApproveAppointments from "../ApproveAppoinments/ApproveAppointments";
import Appointments from "../ApproveAppoinments/Appointments";

const DoctorDashboard = () => {
  const [selectedMenu, setSelectedMenu] = useState("Dashboard");
  const [doctorEmail, setDoctorEmail] = useState("");
  const [notifications, setNotifications] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [doctorProfile, setDoctorProfile] = useState({
    doctorName: "",
    speciality: "",
    location: "",
    mobileNo: "",
    hospitalName: "",
    chargedPerVisit: "",
  });

  const socketUrl = "http://localhost:8080/ws"; // Your WebSocket endpoint URL

  // Fetch doctor's email
  const fetchDoctorEmail = () => {
    axios
      .get("http://localhost:8080/api/doctor/get-welcome-email")
      .then((response) => {
        setDoctorEmail(response.data.email || "Unknown Email");
      })
      .catch((error) => {
        console.error("Error fetching email:", error);
        setDoctorEmail("Error fetching email");
      });
  };

  // Fetch doctor's profile
  const fetchDoctorProfile = () => {
    axios
      .get("http://localhost:8080/api/doctor/profile")
      .then((response) => {
        setDoctorProfile(response.data);
      })
      .catch((error) => {
        console.error("Error fetching profile:", error);
      });
  };

  // Fetch notifications
  const fetchNotifications = useCallback(() => {
    if (doctorEmail) {
      axios
        .get(`http://localhost:8080/notifications/${doctorEmail}`)
        .then((response) => {
          setNotifications(response.data);
        })
        .catch((error) => {
          console.error("Error fetching notifications:", error);
        });
    }
  }, [doctorEmail]);

  // Mark notification as read
  const markAsRead = (id) => {
    axios
      .post(`http://localhost:8080/notifications/read/${id}`)
      .then(() => {
        setNotifications(notifications.filter((n) => n.id !== id));
      })
      .catch((error) => console.error("Error marking notification as read:", error));
  };

  // WebSocket for real-time notifications
  useEffect(() => {
    if (doctorEmail) {
      const socket = new SockJS(socketUrl);
      const stompClient = Stomp.over(socket);

      stompClient.connect({}, () => {
        stompClient.subscribe(`/topic/notifications/${doctorEmail}`, (message) => {
          const newNotification = JSON.parse(message.body);
          setNotifications((prevNotifications) => [newNotification, ...prevNotifications]);
        });
      });

      return () => {
        if (stompClient) {
          stompClient.disconnect();
        }
      };
    }
  }, [doctorEmail]);

  // Initial data fetch
  useEffect(() => {
    fetchDoctorEmail();
    fetchDoctorProfile();
  }, []);

  useEffect(() => {
    if (doctorEmail) {
      fetchNotifications();
    }
  }, [doctorEmail, fetchNotifications]);

  const handleMenuClick = (menu) => {
    setSelectedMenu(menu);
  };

  return (
    <div className="dashboard-container">
      <div className="sidebar">
        <div className="profile-section">
          <img src="assets/img/maledoctor.png" alt="Doctor" className="profile-picture" />
          <p className="doctor-name">
            {doctorProfile.doctorName || "Loading Name..."}
          </p>
        </div>
        <ul className="menu-list">
          {["Dashboard", "Edit Profile", "Your Schedule", "Approve Appointments", "Accepted Appointments", "Add Prescriptions"].map((menu) => (
            <li
              key={menu}
              className={selectedMenu === menu ? "menu-item active" : "menu-item"}
              onClick={() => handleMenuClick(menu)}
            >
              {menu}
            </li>
          ))}
        </ul>
      </div>

      <div className="main-content">
        <div className="Doctor-navbar">
          <ul>
            <li><a href="/doctor-dashboard">Home</a></li>
            <li><span>Welcome {doctorEmail || "Loading..."}</span></li>
            <li>
              <div className="notification-container">
                <button
                  className="notification-bell"
                  onClick={() => setShowNotifications(!showNotifications)}
                >
                  🔔
                  {notifications.length > 0 && (
                    <span className="notification-badge">{notifications.length}</span>
                  )}
                </button>
                {showNotifications && (
                  <div className="notification-dropdown">
                    {notifications.length > 0 ? (
                      notifications.map((notification) => (
                        <div key={notification.id} className="notification-item">
                          <p>{notification.message}</p>
                          {notification.status !== "READ" && (
                            <button onClick={() => markAsRead(notification.id)}>
                              Mark as Read
                            </button>
                          )}
                        </div>
                      ))
                    ) : (
                      <p>No new notifications</p>
                    )}
                  </div>
                )}
              </div>
            </li>
            <li><a href="/WelcomePage">Logout</a></li>
          </ul>
        </div>

        <div className="cards-container">
          {selectedMenu === "Dashboard" && (
            <>
              <div className="card">
                <h2>Appointments</h2>
                <p>2</p>
              </div>
              <div className="card">
                <h2>Total Patients</h2>
                <p>3 patients</p>
              </div>
              <div className="card">
                <h2>Total Slots</h2>
                <p>3 slots</p>
              </div>
            </>
          )}
          {selectedMenu === "Edit Profile" && (
            <EditProfile doctorProfile={doctorProfile} setDoctorProfile={setDoctorProfile} />
          )}
          {selectedMenu === "Your Schedule" && <YourSchedule />}
          {selectedMenu === "Approve Appointments" && <ApproveAppointments />}
          {selectedMenu === "Accepted Appointments" && <Appointments />}
        </div>
      </div>
    </div>
  );
};

export default DoctorDashboard;

