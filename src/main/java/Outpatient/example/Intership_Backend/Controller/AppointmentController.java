
package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Repository.AppointmentRepository;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import Outpatient.example.Intership_Backend.Repository.PatientRepository;
import Outpatient.example.Intership_Backend.Service.AppointmentService;
import Outpatient.example.Intership_Backend.Service.DoctorService;
import Outpatient.example.Intership_Backend.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private DoctorRepository doctorRepository;  // Autowire the DoctorRepository
    //change
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorService doctorService;
    @PostMapping("/book")
    public ResponseEntity<String> bookAppointment(@RequestBody Appointment appointment) {
        try {
            // Fetch doctor details based on the doctor's email from appointment
            Optional<Doctor> doctorOptional = doctorRepository.findById(appointment.getDoctorEmail());

            if (!doctorOptional.isPresent()) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.BAD_REQUEST);
            }

            Doctor doctor = doctorOptional.get();
            double chargedPerVisit = doctor.getChargedPerVisit(); // Get charged per visit from the Doctor entity

            // Book the appointment
            Appointment savedAppointment = appointmentService.bookAppointment(appointment);

            // Handle payment mode logic
            if ("CASH".equalsIgnoreCase(appointment.getPaymentmode())) {
                // If payment is Cash, mark the appointment as paid directly (or any other logic as needed)

                return new ResponseEntity<>("Appointment booked Successfully", HttpStatus.CREATED);
            } else if ("ONLINE_PAY".equalsIgnoreCase(appointment.getPaymentmode())) {
                // If payment is Online, generate the payment URL
                String paymentUrl = paymentService.createCheckoutSession(savedAppointment.getId().toString(), chargedPerVisit);
                return new ResponseEntity<>(paymentUrl, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid payment mode", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //right
    @GetMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(@RequestParam String appointmentId) {
        // Mark the appointment as "Paid" or perform necessary post-payment updates
        URI redirectUri = URI.create("http://localhost:3000/payment-success?appointmentId=" + appointmentId);
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();

        // return ResponseEntity.ok("Payment successful for appointment ID: " + appointmentId);
    }
//right

    @GetMapping("/payment-cancelled")
    public ResponseEntity<String> paymentCancelled() {
        return ResponseEntity.ok("Payment was cancelled.");
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

//add
    @PostMapping("/appointments/{appointmentId}/approve")
    public String approveAppointment(@PathVariable int appointmentId) {
        try {
            doctorService.approveAppointment(appointmentId);  // Call the approve method
            return "Appointment accepted and notification sent!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }



}
    //till right code



