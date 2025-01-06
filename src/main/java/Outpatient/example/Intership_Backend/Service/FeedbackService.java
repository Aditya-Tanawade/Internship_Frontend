package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Feedback;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Repository.AppointmentRepository;
import Outpatient.example.Intership_Backend.Repository.FeedbackRepository;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import Outpatient.example.Intership_Backend.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    @Autowired
    private  AppointmentRepository appointmentRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.feedbackRepository = feedbackRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;

    }

    @Autowired
    private PatientService patientService;
    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public String getDoctorNameByAppointmentId(Long appointmentId) {

        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            Doctor doctor = doctorRepository.findByEmail(appointment.get().getDoctorEmail());
            return doctor.getDoctorName();
        }else {
            throw new RuntimeException("Appointment not found!");
        }
    }

    public String getPatientNameByPatientId() {
        Patient patient = patientRepository.findByEmail(patientService.loginEmail);
        return patient.getPatientName();
    }


    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback addLikeToFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setLikes(feedback.getLikes() + 1);
        return feedbackRepository.save(feedback);
    }
}
