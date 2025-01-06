package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.Entity.Feedback;
import Outpatient.example.Intership_Backend.Service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Submit feedback
    @PostMapping("/add")
    public ResponseEntity<Feedback> addFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.saveFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }

    // Get doctor's name by appointment ID
    @GetMapping("/{appointmentId}")
    public ResponseEntity<String> getDoctorNameByAppointmentId(@PathVariable Long appointmentId) {
        String doctorName = feedbackService.getDoctorNameByAppointmentId(appointmentId);
        return ResponseEntity.ok(doctorName);
    }

    // Get patient's name by patient ID
    @GetMapping("/patient-name")
    public ResponseEntity<String> getPatientNameByPatientId() {
        String patientName = feedbackService.getPatientNameByPatientId();
        return ResponseEntity.ok(patientName);
    }

    @GetMapping("/get-all-feedbacks")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @PostMapping("/like/{feedbackId}")
    public Feedback likeFeedback(@PathVariable Long feedbackId) {
        return feedbackService.addLikeToFeedback(feedbackId);
    }

}
