package Outpatient.example.Intership_Backend.Controller;


import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Service.AppointmentService;
import Outpatient.example.Intership_Backend.Service.DoctorService;
import Outpatient.example.Intership_Backend.Service.PatientService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor/")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;




    @GetMapping("/get-welcome-email")
    public ResponseEntity<Map<String, String>> getWelcomeEmail() {
        Map<String, String> response = new HashMap<>();
        response.put("email", doctorService.getLoginEmail());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile")
    public ResponseEntity<Doctor> getDoctorProfile() {
        Doctor doctor = doctorService.getDoctorProfile();
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }


    @PutMapping("/edit-profile")
    public ResponseEntity<ApiError> editDoctorProfile(@RequestBody @Valid Doctor doctor) {
        return doctorService.updateDoctorProfile(doctor);
    }


    @GetMapping("/get-all-doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }


    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientEmail() {
        try {
            List<Appointment> appointments = doctorService.getAppointments();
            return ResponseEntity.ok(appointments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


//correct


    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
        try {
            boolean isCancelled = doctorService.cancelAppointment(id);
            if (isCancelled) {
                return ResponseEntity.ok("Appointment cancelled successfully.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending cancellation email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/appointments/approve/{id}")
    public ResponseEntity<String> approveAppointment(@PathVariable int id) {
        try {
            doctorService.approveAppointment(id);
            return ResponseEntity.ok("Appointment approved successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending approval email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/appointments/reject/{id}")
    public ResponseEntity<String> rejectAppointment(@PathVariable int id) {
        try {
            doctorService.rejectAppointment(id);
            return ResponseEntity.ok("Appointment rejected successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending rejection email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/appointments/complete/{id}")
    public ResponseEntity<String> completeAppointment(@PathVariable int id) {
        try {
            doctorService.completeAppointment(id);
            return ResponseEntity.ok("Appointment completed successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending completion email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/accepted-appointments")
    public ResponseEntity<List<Appointment>> getAcceptedAppointments() {
        try {
            List<Appointment> acceptedAppointments = doctorService.getAcceptedAppointments();
            return ResponseEntity.ok(acceptedAppointments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/appointment-requests-count")
    public ResponseEntity<?> getAppointmentRequestsCount() {
        int count = doctorService.getPendingAppointmentRequestsCountByDoctor();
        return ResponseEntity.ok().body(Map.of("count", count));
    }

    @GetMapping("/accepted-appointments-count")
    public ResponseEntity<?> getAcceptedAppointmentsCount() {
        int count = doctorService.getAcceptedAppointmentsCountByDoctor();
        return ResponseEntity.ok().body(Map.of("count", count));
    }

    @GetMapping("/completed-appointments-count")
    public ResponseEntity<?> getCompletedAppointmentsCount() {
        int count = doctorService.getCompletedAppointmentsCountByDoctor();
        return ResponseEntity.ok().body(Map.of("count", count));
    }

//    @GetMapping("/patients-count")
//    public ResponseEntity<?> getPatientsCount() {
//        int count = doctorService.getTotalPatientsCountByDoctor();
//        return ResponseEntity.ok().body(Map.of("count", count));
//    }


    




}