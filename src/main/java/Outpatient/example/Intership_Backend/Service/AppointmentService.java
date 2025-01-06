package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Controller.NotificationController;
import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Notification;
import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Repository.AppointmentRepository;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import Outpatient.example.Intership_Backend.Repository.NotificationRepository;
import Outpatient.example.Intership_Backend.Repository.PatientRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;


    @Autowired
    private NotificationRepository notificationRepository;


    @Autowired
    private NotificationService notificationService;


    @Autowired
    private NotificationController notificationController;
//
//    public Appointment bookAppointment(Appointment appointment) {
//        Patient patient = patientRepository.findByEmail(patientService.getLoginEmail());
//
//
//        Doctor doctor = doctorRepository.findByEmail(appointment.getDoctorEmail());
//        if (doctor == null) {
//            throw new IllegalArgumentException("Doctor with email " + appointment.getDoctorEmail() + " does not exist.");
//        }
//
//
//        appointment.setPatient(patient);
//        return appointmentRepository.save(appointment);
//    }
    //till right




//
//    public Appointment bookAppointment(Appointment appointment) throws MessagingException {
//        // Fetch the patient by their email (from the PatientService)
//        Patient patient = patientRepository.findByEmail(patientService.getLoginEmail());
//
//        // Fetch the doctor using the email provided in the appointment request
//        Doctor doctor = doctorRepository.findByEmail(appointment.getDoctorEmail());
//        if (doctor == null) {
//            throw new IllegalArgumentException("Doctor with email " + appointment.getDoctorEmail() + " does not exist.");
//        }
//
//        // Set the patient for the appointment
//        appointment.setPatient(patient);
//
//        // Save the appointment to the database
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//
//        // Send the email notification to the doctor about the new appointment
//        String emailSubject = "New Appointment Booked";
//        String emailBody = "A new appointment has been booked with you by patient " + patient.getPatientName() +
//                " on " + savedAppointment.getAppointmentDate() +
//                ".\nReason for appointment: " + savedAppointment.getReason();
//        notificationService.sendEmail(doctor.getEmail(), emailSubject, emailBody);
//
//        // Create a notification for the patient regarding the appointment booking
//        Notification notification = new Notification();
//        notification.setRecipientEmail(patient.getEmail()); // Set the patient’s email
//        notification.setSubject("Appointment Booked Successfully");
//        notification.setMessage("Your appointment with Dr. " + doctor.getDoctorName() + " has been successfully booked for " +
//                savedAppointment.getAppointmentDate() + ". Reason: " + savedAppointment.getReason());
//        notification.setStatus("SENT"); // Mark the notification status as SENT
//        notification.setTimestamp(LocalDateTime.now()); // Set the current timestamp
//
//        // Save the notification to the database
//        notificationRepository.save(notification);
//
//        // Return the saved appointment details
//        return savedAppointment;
//    }



        public Appointment bookAppointment(Appointment appointment) {
            Patient patient = patientRepository.findByEmail(patientService.getLoginEmail());

            Doctor doctor = doctorRepository.findByEmail(appointment.getDoctorEmail());
            if (doctor == null) {
                throw new IllegalArgumentException("Doctor with email " + appointment.getDoctorEmail() + " does not exist.");
            }

            // Set the patient for the appointment
            appointment.setPatient(patient);

            Appointment savedAppointment = appointmentRepository.save(appointment);

            Notification notification = new Notification();
            notification.setRecipientEmail(doctor.getEmail()); // Doctor's email
            notification.setSubject("New Appointment Booked");
            notification.setMessage("A new appointment has been booked with you by patient " + patient.getPatientName() +
                    " on " + savedAppointment.getAppointmentDate() +
                    ".\nReason for appointment: " + savedAppointment.getReason());
            notification.setStatus("PENDING"); // Initially set status to "PENDING"
            notification.setTimestamp(LocalDateTime.now()); // Set the current timestamp

            // Save the notification to the database
            Notification savedNotification = notificationRepository.save(notification);

            // Now send the email to the doctor
            try {
                // Use the injected NotificationService to send the email
                notificationService.sendEmail(doctor.getEmail(), "New Appointment Booked",
                        "A new appointment has been booked with you by patient " + patient.getPatientName() +
                                " on " + savedAppointment.getAppointmentDate());

                // If the email is successfully sent, update the status of the notification
                //savedNotification.setStatus("SENT");
                savedNotification.setStatus("UNREAD");
                notificationRepository.save(savedNotification); // Save the updated notification status
            } catch (MessagingException e) {
                // If there's an error sending the email, keep the status as "PENDING" or set it to "FAILED"
                savedNotification.setStatus("FAILED");
                notificationRepository.save(savedNotification);
                // Optionally, log the error or handle it based on your use case

                //add new bell icon
                // Trigger the notification
                //emailService.sendNotification(appointment.getPatient().getEmail(), message);
                notificationController.sendNotification(
                        appointment.getPatient().getEmail(),
                        //"Your appointment with Dr. " + appointment.getDoctorEmail()+ " has been Completed."
                        "A new appointment has been booked with you by patient " + patient.getPatientName() +
                                " on "+ savedAppointment.getAppointmentDate()
                );
            }

            // Return the saved appointment
            return savedAppointment;
        }




    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(int id) {
        return appointmentRepository.findById(id);
    }

    public void cancelAppointment(int id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {

            Appointment existingAppointment = appointment.get();
            existingAppointment.setStatus("Cancelled");
            appointmentRepository.save(existingAppointment);
        } else {
            throw new IllegalArgumentException("Appointment with ID " + id + " does not exist.");
        }
    }

public Appointment finalizeAppointment(String appointmentId) {
    Optional<Appointment> optionalAppointment = appointmentRepository.findById(Integer.valueOf(appointmentId));

    if (!optionalAppointment.isPresent()) {
        throw new IllegalArgumentException("Invalid appointment ID.");
    }

    Appointment appointment = optionalAppointment.get();
    appointment.setStatus("BOOKED"); // Mark the appointment as booked
    return appointmentRepository.save(appointment);
}


}
