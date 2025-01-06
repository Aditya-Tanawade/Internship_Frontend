package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Entity.Payment;
import Outpatient.example.Intership_Backend.Repository.AppointmentRepository;
import Outpatient.example.Intership_Backend.Repository.PatientRepository;
import Outpatient.example.Intership_Backend.Repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PatientRepository patientRepository; // Assuming you have this to fetch patient by email

    @Autowired
    private AppointmentRepository appointmentRepository; // Assuming you have this to fetch appointment by ID

    public String createCheckoutSession(String appointmentId, double chargePerVisit) {
        Stripe.apiKey = stripeApiKey;

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:8080/appointments/payment-success?appointmentId=" + appointmentId)
                    .setCancelUrl("http://localhost:8080/appointments/payment-cancelled")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("inr")
                                                    .setUnitAmount((long) (chargePerVisit * 100)) // Use chargePerVisit
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Appointment Booking Fee")
                                                                    .build())
                                                    .build())
                                    .setQuantity(1L)
                                    .build())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Stripe payment initiation failed", e);
        }
    }

public boolean verifyPayment(String sessionId) {
    try {
        Session session = Session.retrieve(sessionId);
        return "paid".equalsIgnoreCase(session.getPaymentStatus());
    } catch (Exception e) {
        return false;
    }
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



}