package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.Controller.NotificationController;
import Outpatient.example.Intership_Backend.DTO.LoginRequest;
import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Notification;
import Outpatient.example.Intership_Backend.Repository.AppointmentRepository;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import Outpatient.example.Intership_Backend.Repository.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService emailService;

    @Autowired
    private NotificationController notificationController;

    String loginEmail;
    String registerEmail;




    public void createDoctor(RegisterUserDTo registerUserDTo) {

        Doctor doctor = new Doctor();
        registerEmail= registerUserDTo.getEmail();
        doctor.setEmail(registerUserDTo.getEmail());
        doctorRepository.save(doctor);
    }






    public ResponseEntity<ApiError> updateDoctorProfile(Doctor doctorDTO) {

        System.out.println(loginEmail);
        Doctor existingDoctor = doctorRepository.findByEmail(loginEmail);

        if (existingDoctor == null) {
            ApiError errorResponse = ApiError.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Doctor not found or unauthorized access")
                    .subErrors(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        existingDoctor.setDoctorName(doctorDTO.getDoctorName());
        existingDoctor.setSpeciality(doctorDTO.getSpeciality());
        existingDoctor.setLocation(doctorDTO.getLocation());
        existingDoctor.setMobileNo(doctorDTO.getMobileNo());
        existingDoctor.setHospitalName(doctorDTO.getHospitalName());
        existingDoctor.setChargedPerVisit(doctorDTO.getChargedPerVisit());

        doctorRepository.save(existingDoctor);


        ApiError successResponse = ApiError.builder()
                .status(HttpStatus.OK)
                .message("Doctor profile updated successfully")
                .subErrors(null)
                .build();
        return ResponseEntity.ok(successResponse);
    }

    // Login Doctor
    public void loginDoctor(LoginRequest loginRequest) {
        loginEmail = loginRequest.getEmail();
    }

    // Get Doctor Profile
    public Doctor getDoctorProfile() {
        if (loginEmail == null || loginEmail.isEmpty()) {
            throw new IllegalArgumentException("No doctor is currently logged in.");
        }
        return doctorRepository.findByEmail(loginEmail);
    }

    // Get All Doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Fetch Appointments for the Logged-In Doctor
    public List<Appointment> getAppointmentsByDoctorEmail() {
        if (loginEmail == null || loginEmail.isEmpty()) {
            throw new IllegalArgumentException("No doctor is currently logged in.");
        }
        return appointmentRepository.findByDoctorEmail(loginEmail);
    }

    // Fetch Accepted Appointments
    public List<Appointment> getAcceptedAppointments() {
        if (loginEmail == null || loginEmail.isEmpty()) {
            throw new IllegalArgumentException("No doctor is currently logged in.");
        }
        return appointmentRepository.findByDoctorEmailAndStatus(loginEmail, "ACCEPTED");
    }

    // Cancel Appointment
    public boolean cancelAppointment(int appointmentId) throws MessagingException {
        if (appointmentRepository.existsById(appointmentId)) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
            appointmentRepository.delete(appointment);

            // Create Notification for Cancelled Appointment
            Notification notification = new Notification();
            notification.setRecipientEmail(appointment.getPatient().getEmail());
            notification.setSubject("Appointment Cancelled");
            notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
                    "Your appointment with Dr. " + appointment.getDoctorEmail() +
                    " on " + appointment.getAppointmentDate() + " has been cancelled.\n\nThank you!");
            notification.setStatus("PENDING");
            notification.setTimestamp(LocalDateTime.now());
            notificationRepository.save(notification);

            // Send Email
            emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
            notification.setStatus("UNREAD");
            notificationRepository.save(notification);


            //add new bell icon
            // Trigger the notification
            //emailService.sendNotification(appointment.getPatient().getEmail(), message);
            notificationController.sendNotification(
                    appointment.getPatient().getEmail(),
                    //"Your appointment with Dr. " + appointment.getDoctorEmail()+ " has been Completed."
                    "An Appointment Cancelled by " + appointment.getPatient().getPatientName() +
                            " on "+ appointment.getAppointmentDate()
            );

            return true;
        }
        return false;
    }
    public void approveAppointment(int appointmentId) throws MessagingException {
        Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appointment.setStatus("ACCEPTED");
        appointmentRepository.save(appointment);

        // Create Notification
        Notification notification = new Notification();
        notification.setRecipientEmail(appointment.getPatient().getEmail());
        notification.setSubject("Appointment Accepted");
        notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
                "Your appointment with Dr Email " + appointment.getDoctorEmail() +
                " on " + appointment.getAppointmentDate() + " has been accepted.\n\nThank you!");
        notification.setStatus("PENDING");
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);

        // Send Email
        emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
        notification.setStatus("UNREAD");
        notificationRepository.save(notification);

        //add new bell icon
        // Trigger the notification
        //emailService.sendNotification(appointment.getPatient().getEmail(), message);
        notificationController.sendNotification(
                appointment.getPatient().getEmail(),
                "Your appointment with Dr. " + appointment.getDoctorEmail()+ " has been accepted."
        );
    }

    public void rejectAppointment(int appointmentId) throws MessagingException {
        Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appointment.setStatus("REJECTED");
        appointmentRepository.save(appointment);

        // Create Notification
        Notification notification = new Notification();
        notification.setRecipientEmail(appointment.getPatient().getEmail());
        notification.setSubject("Appointment Rejected");
        notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
                "We regret to inform you that your appointment with Dr. " + appointment.getDoctorEmail() +
                " on " + appointment.getAppointmentDate() + " has been rejected.\n\nThank you!");
        notification.setStatus("PENDING");
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);

        // Send Email
        emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
        //notification.setStatus("SENT");
        notification.setStatus("UNREAD");
        notificationRepository.save(notification);

        //add new bell icon
        // Trigger the notification
        //emailService.sendNotification(appointment.getPatient().getEmail(), message);
        notificationController.sendNotification(
                appointment.getPatient().getEmail(),
                "Your appointment with Dr. " + appointment.getDoctorEmail()+ " has been rejected."
        );
    }

    public void completeAppointment(int appointmentId) throws MessagingException {
        Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        appointment.setStatus("COMPLETED");
        appointmentRepository.save(appointment);

        // Create Notification
        Notification notification = new Notification();
        notification.setRecipientEmail(appointment.getPatient().getEmail());
        notification.setSubject("Appointment Completed");
        notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
                "Your appointment with Doctor Email " + appointment.getDoctorEmail() +
                " on " + appointment.getAppointmentDate() + " has been successfully completed.\n\nThank you!");
        notification.setStatus("PENDING");
        notification.setTimestamp(LocalDateTime.now());
        notificationRepository.save(notification);

        // Send Email
        emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
        notification.setStatus("UNREAD");
        notificationRepository.save(notification);

        //add new bell icon
        // Trigger the notification
        //emailService.sendNotification(appointment.getPatient().getEmail(), message);
        notificationController.sendNotification(
                appointment.getPatient().getEmail(),
                "Your appointment with Dr. " + appointment.getDoctorEmail()+ " has been Completed."
        );
    }


        public List<Appointment> getAppointments() {
        String doctorEmail = getLoginEmail(); // Replace with your authentication logic
        return appointmentRepository.findByDoctorEmailAndStatusIsNull(doctorEmail);
    }


//    public boolean cancelAppointment(int appointmentId) {
//        if (appointmentRepository.existsById((Integer) appointmentId)) {
//            Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
//                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//            appointmentRepository.delete(appointment);
//
//            // Create Notification for Cancelled Appointment
//            Notification notification = new Notification();
//            notification.setRecipientEmail(appointment.getPatient().getEmail());
//            notification.setSubject("Appointment Cancelled");
//            notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
//                    "Your appointment with Dr. " + appointment.getDoctorEmail() +
//                    " on " + appointment.getAppointmentDate() + " has been cancelled.\n\nThank you!");
//            notification.setStatus("PENDING");
//            notification.setTimestamp(LocalDateTime.now());
//            notificationRepository.save(notification);
//
//            // Send Email
//            emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
//            notification.setStatus("SENT");
//            notificationRepository.save(notification);
//
//            return true;
//        }
//        return false;
//    }

    // Approve, Reject, and Complete Appointment Methods (Already Provided Above)
// Approve Appointment with Notification
//    public void approveAppointment(int appointmentId) {
//        Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//        appointment.setStatus("ACCEPTED");
//        appointmentRepository.save(appointment);
//
//        // Create Notification
//        Notification notification = new Notification();
//        notification.setRecipientEmail(appointment.getPatient().getEmail());
//        notification.setSubject("Appointment Accepted");
//        notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
//                "Your appointment with Dr. " + appointment.getDoctorEmail() +
//                " on " + appointment.getAppointmentDate() + " has been accepted.\n\nThank you!");
//        notification.setStatus("PENDING");
//        notification.setTimestamp(LocalDateTime.now());
//        notificationRepository.save(notification);
//
//        // Send Email
//        emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
//        notification.setStatus("SENT");
//        notificationRepository.save(notification);
//    }
//
//    // Reject Appointment with Notification
//    public void rejectAppointment(int appointmentId) {
//        Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//        appointment.setStatus("REJECTED");
//        appointmentRepository.save(appointment);
//
//        // Create Notification
//        Notification notification = new Notification();
//        notification.setRecipientEmail(appointment.getPatient().getEmail());
//        notification.setSubject("Appointment Rejected");
//        notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
//                "We regret to inform you that your appointment with Dr. " + appointment.getDoctorEmail() +
//                " on " + appointment.getAppointmentDate() + " has been rejected.\n\nThank you!");
//        notification.setStatus("PENDING");
//        notification.setTimestamp(LocalDateTime.now());
//        notificationRepository.save(notification);
//
//        // Send Email
//        emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
//        notification.setStatus("SENT");
//        notificationRepository.save(notification);
//    }
//
//    // Complete Appointment with Notification
//    public void completeAppointment(int appointmentId) {
//        Appointment appointment = appointmentRepository.findById((Integer) appointmentId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//        appointment.setStatus("COMPLETED");
//        appointmentRepository.save(appointment);
//
//        // Create Notification
//        Notification notification = new Notification();
//        notification.setRecipientEmail(appointment.getPatient().getEmail());
//        notification.setSubject("Appointment Completed");
//        notification.setMessage("Dear " + appointment.getPatient().getPatientName() + ",\n\n" +
//                "Your appointment with Dr. " + appointment.getDoctorEmail() +
//                " on " + appointment.getAppointmentDate() + " has been successfully completed.\n\nThank you!");
//        notification.setStatus("PENDING");
//        notification.setTimestamp(LocalDateTime.now());
//        notificationRepository.save(notification);
//
//        // Send Email
//        emailService.sendEmail(notification.getRecipientEmail(), notification.getSubject(), notification.getMessage());
//        notification.setStatus("SENT");
//        notificationRepository.save(notification);
//    }
    // Mark Notification as Read (Optional Feature)
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setStatus("READ");
        notificationRepository.save(notification);
    }




//    public int getPendingAppointmentRequestsCountByDoctor(String doctorEmail) {
//        return appointmentRepository.countByStatusAndDoctorEmail("", doctorEmail);
//
//    }

    public int getPendingAppointmentRequestsCountByDoctor() {
        return appointmentRepository.countByStatusIsNullAndDoctorEmail(loginEmail);
    }


    public int getAcceptedAppointmentsCountByDoctor() {
        return appointmentRepository.countByStatusAndDoctorEmail("ACCEPTED", loginEmail);
    }

    public int getCompletedAppointmentsCountByDoctor() {
        return appointmentRepository.countByStatusAndDoctorEmail("COMPLETED", loginEmail);
    }


//    public int getTotalPatientsCountByDoctor() {
//        return appointmentRepository.countDistinctPatientsAndStatusIsNullByDoctorEmail(loginEmail);
//    }
}

