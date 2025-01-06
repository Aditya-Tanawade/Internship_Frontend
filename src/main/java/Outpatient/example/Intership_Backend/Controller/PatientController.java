package Outpatient.example.Intership_Backend.Controller;


import Outpatient.example.Intership_Backend.Advices.ApiError;
import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Service.PatientService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/get-welcome-email")
    public ResponseEntity<Map<String, String>> getWelcomeEmail() {
        Map<String, String> response = new HashMap<>();
        response.put("email", patientService.getLoginEmail());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile")
    public ResponseEntity<Patient> getDoctorProfile() {
        Patient patient = patientService.getPatientProfile();
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }


    @PutMapping("/edit-profile")
    public ResponseEntity<ApiError> editDoctorProfile(@RequestBody @Valid Patient patient) {
        return patientService.updateDoctorProfile(patient);
    }

//    @GetMapping("/appointments")
//    public List<Appointment> getAppointmentsByPatientEmail() {
//        return patientService.getAppointmentsByPatientEmail();
//    }



    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatientEmail() {
        try {
            List<Appointment> appointments = patientService.getAppointmentsByPatientEmail();
            return ResponseEntity.ok(appointments);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


//    @DeleteMapping("/appointments/{id}")
//    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
//        boolean isCancelled = patientService.cancelAppointment(id);
//        if (isCancelled) {
//            return ResponseEntity.ok("Appointment cancelled successfully.");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
//    }
//till right
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
        try {
            boolean isCancelled = patientService.cancelAppointment(id);
            if (isCancelled) {
                return ResponseEntity.ok("Appointment cancelled successfully.");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email notification: " + e.getMessage());
        }
    }


    @GetMapping("/get-all-patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/pending-appointments-count")
    public ResponseEntity<?> getAppointmentRequestsCount() {
        int count = patientService.getPendingAppointmentRequestsCountByPatient();
        return ResponseEntity.ok().body(Map.of("count", count));
    }

    @GetMapping("/completed-appointments-count")
    public ResponseEntity<?> getAcceptedAppointmentsCount() {
        int count = patientService.getCompletedAppointmentsCountByPatient();
        return ResponseEntity.ok().body(Map.of("count", count));
    }




}