package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.Controller.NotificationController;
import Outpatient.example.Intership_Backend.DTO.LoginRequest;
import Outpatient.example.Intership_Backend.DTO.RegisterUserDTo;
import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Notification;
import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Entity.Payment;
import Outpatient.example.Intership_Backend.Repository.*;
import jakarta.mail.MessagingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Data
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JavaMailSender mailSender;  // Add this for sending emails


    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private NotificationController notificationController;


    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;



    String registerEmail;
    String loginEmail;

    // Existing methods...



    public void createPatient(RegisterUserDTo registerUserDTo) {
        Patient patient = new Patient();
        registerEmail= registerUserDTo.getEmail();
        patient.setEmail(registerUserDTo.getEmail());
        patientRepository.save(patient);
    }



    public void loginPatient(LoginRequest loginRequest) {
        loginEmail= loginRequest.getEmail();
    }

    public Patient getPatientProfile() {
        return patientRepository.findByEmail(loginEmail);// Return null if doctor is not found
    }


    public Patient getPatientProfile(String email) {
        return patientRepository.findByEmail(loginEmail);// Return null if doctor is not found
    }

    public List<Patient> getAllDoctors() {
        return patientRepository.findAll();
    }


    public ResponseEntity<ApiError> updateDoctorProfile(Patient patientDto) {

        System.out.println(loginEmail);
        Patient existingPatient = patientRepository.findByEmail(loginEmail);

        if (existingPatient == null) {
            ApiError errorResponse = ApiError.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Patient not found ")
                    .subErrors(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        existingPatient.setPatientName(patientDto.getPatientName());
        existingPatient.setAge(patientDto.getAge());
        existingPatient.setGender(patientDto.getGender());
        existingPatient.setMobileNo(patientDto.getMobileNo());
        existingPatient.setBloodGroup(patientDto.getBloodGroup());
        existingPatient.setAddress(patientDto.getAddress());

        patientRepository.save(existingPatient);


        ApiError successResponse = ApiError.builder()
                .status(HttpStatus.OK)
                .message("Patient profile updated successfully")
                .subErrors(null)
                .build();
        return ResponseEntity.ok(successResponse);
    }



    public List<Appointment> getAppointmentsByPatientEmail() {
        if (loginEmail == null || loginEmail.isEmpty()) {
            throw new IllegalArgumentException("No patient is currently logged in.");
        }
        return appointmentRepository.findByPatientEmail(loginEmail);
    }

    public boolean cancelAppointment(int appointmentId) throws MessagingException {
        if (appointmentRepository.existsById(appointmentId)) {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

            // Check if the status is "ACCEPTED"
            if ("ACCEPTED".equals(appointment.getStatus())) {
                String patientEmail = appointment.getPatient().getEmail();
                String doctorEmail = appointment.getDoctorEmail();

                // Send the cancellation email to the doctor
                sendCancellationEmailToDoctor(doctorEmail, appointment);

                // Notify the patient about the cancellation
                Notification notification = new Notification();

                notification.setRecipientEmail(doctorEmail);
                notification.setSubject("Appointment Cancellation Notice");


                notification.setMessage( "The appointment with patient " + appointment.getPatient().getPatientName()+ " has been cancelled.");
                notification.setStatus("UNREAD");
                notification.setTimestamp(LocalDateTime.now());
                notificationRepository.save(notification);

                notificationController.sendNotification(
                        doctorEmail, // Notify the doctor dashboard
                        "The appointment with patient " + appointment.getPatient().getPatientName()+ " has been cancelled."
                );


            }

            // Delete the appointment regardless of status
            appointmentRepository.deleteById(appointmentId);
            return true;
        }
        return false;
    }



    private void sendCancellationEmailToDoctor(String doctorEmail, Appointment appointment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(doctorEmail);
        message.setSubject("Appointment Cancellation Notification");
        message.setText("Dear Doctor,\n\nThe patient has cancelled their appointment scheduled on " +
                appointment.getAppointmentDate() + ".\n\nBest regards,\nYour Health System");

        // Send the email
        mailSender.send(message);
    }

    public void savePaymentDetails(double amount, String appointmentId, String doctorEmail, String patientEmail) {
        // Fetch the patient by their email
        Patient patient = patientRepository.findByEmail(patientEmail);

        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        // Create a new Payment record
        Payment payment = new Payment();
        payment.setAmount(amount); // Set the payment amount
        payment.setAppointmentId(Long.parseLong(appointmentId)); // Set the appointment ID
        payment.setDoctorEmail(doctorEmail); // Set the doctor's email
        payment.setPatient(patient); // Link the patient with the payment

        // Save the payment record in the database
        paymentRepository.save(payment);
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }


    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public int getPendingAppointmentRequestsCountByPatient() {
        return appointmentRepository.countByStatusIsNullAndPatientEmail(loginEmail);
    }


    public int getCompletedAppointmentsCountByPatient() {
        return appointmentRepository.countByStatusAndPatientEmail("COMPLETED",loginEmail);
    }
}
