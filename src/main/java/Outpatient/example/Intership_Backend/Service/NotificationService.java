package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.Entity.Notification;
import Outpatient.example.Intership_Backend.Repository.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;



@Service
public class NotificationService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendEmail(String doctorEmail, String subject, String messageBody) throws MessagingException {

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

    helper.setTo(doctorEmail);
    helper.setSubject(subject);
    helper.setText(messageBody, true);  // true means HTML is allowed

    // Send the email
    javaMailSender.send(mimeMessage);
}

    public void createDoctorActionNotification(String doctorEmail, String patientEmail, String action) {
        Notification notification = new Notification();
        notification.setRecipientEmail(patientEmail);  // Send notification to the patient
        notification.setSubject("Appointment " + action);
        notification.setMessage("Your appointment has been " + action + " by the doctor.");
        notification.setStatus("PENDING");  // You can change this based on your process
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);
    }
    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id " + id));

        notification.setStatus("READ");
        return notificationRepository.save(notification); // Save the updated notification
    }

}

