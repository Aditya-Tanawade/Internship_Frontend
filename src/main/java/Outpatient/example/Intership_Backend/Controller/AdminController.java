



package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/doctors")
    public long getDoctorsCount() {
        return adminService.getDoctorsCount();
    }

    @GetMapping("/patients")
    public long getPatientsCount() {
        return adminService.getPatientsCount();
    }

    @GetMapping("/get-welcome-email")
    public ResponseEntity<Map<String, String>> getWelcomeEmail() {
        Map<String, String> response = new HashMap<>();
        response.put("email", adminService.getLoginEmail());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDoctor(@RequestParam String email) {
        try {
            boolean isDeleted = adminService.deleteDoctorByEmail(email);
            System.out.println(isDeleted);
            if (isDeleted) {
                return ResponseEntity.ok("Doctor deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found with email: " + email);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting doctor.");
        }
    }


    @DeleteMapping("/delete-patient")
    public ResponseEntity<String> deletePatient(@RequestParam String email) {
        try {
            boolean isDeleted = adminService.deletePatientByEmail(email);
            System.out.println(isDeleted);
            if (isDeleted) {
                return ResponseEntity.ok("Patient deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found with email: " + email);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting doctor.");
        }
    }

    //admin by me
    @PostMapping("/add-doctor")
    public ResponseEntity<String> addDoctor(@RequestBody Doctor doctor) {
        try {
            boolean isAdded = adminService.addDoctor(doctor);
            if (isAdded) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Doctor added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor with email already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding doctor.");
        }
    }

    @PostMapping("/add-patient")
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        try {
            boolean isAdded = adminService.addPatient(patient);
            if (isAdded) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Patient added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Patient with email already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding patient.");
        }
    }

}
//
//import Outpatient.example.Intership_Backend.Entity.Appointment;
//import Outpatient.example.Intership_Backend.Service.AdminService;
//import Outpatient.example.Intership_Backend.Service.DoctorService;
//import Outpatient.example.Intership_Backend.Service.PatientService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/admin/")
//public class AdminController {
//
//    @Autowired
//    private AdminService adminService;
//
//
//    @GetMapping("/doctors")
//    public long getDoctorsCount() {
//        return adminService.getDoctorsCount();
//    }
//
//    @GetMapping("/patients")
//    public long getPatientsCount() {
//        return adminService.getPatientsCount();
//    }
//
//    @GetMapping("/appointments")
//    public long getAppointments(){
//        return adminService.getAllAppointmentsCount();
//    }
//
//    @GetMapping("/get-welcome-email")
//    public ResponseEntity<Map<String, String>> getWelcomeEmail() {
//        Map<String, String> response = new HashMap<>();
//        response.put("email", adminService.getLoginEmail());
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteDoctor(@RequestParam String email) {
//        try {
//            boolean isDeleted = adminService.deleteDoctorByEmail(email);
//            System.out.println(isDeleted);
//            if (isDeleted) {
//                return ResponseEntity.ok("Doctor deleted successfully.");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found with email: " + email);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting doctor.");
//        }
//    }
//
//
//    @DeleteMapping("/delete-patient")
//    public ResponseEntity<String> deletePatient(@RequestParam String email) {
//        try {
//            boolean isDeleted = adminService.deletePatientByEmail(email);
//            System.out.println(isDeleted);
//            if (isDeleted) {
//                return ResponseEntity.ok("Patient deleted successfully.");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found with email: " + email);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting doctor.");
//        }
//    }
//
//
//    @GetMapping("/all-appointments")
//    public List<Appointment> getAllAppointments(){
//        return adminService.getAllAppointments();
//    }
//
//    @DeleteMapping("/appointments/{id}")
//    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
//        boolean isDeleted = adminService.deleteAppointment(id);
//        if (isDeleted) {
//            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//
//}
