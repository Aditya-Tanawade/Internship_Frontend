package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.Entity.Notification;
import Outpatient.example.Intership_Backend.Repository.NotificationRepository;
import Outpatient.example.Intership_Backend.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/notifications")

public class NotificationController {

private final SimpMessagingTemplate messagingTemplate;

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;  // Add this to use service


@Autowired
public NotificationController(SimpMessagingTemplate messagingTemplate,
                              NotificationRepository notificationRepository,
                              NotificationService notificationService) {
    this.messagingTemplate = messagingTemplate;
    this.notificationRepository = notificationRepository;
    this.notificationService = notificationService;
}
    // Method to send notification to a specific user
    @PostMapping("/send")  // Allowing notification sending through POST request
    public void sendNotification(@RequestParam String recipientEmail, @RequestParam String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + recipientEmail, message);
    }

    // Get notifications for a specific email
    @GetMapping("/{email}")
    public ResponseEntity<List<Notification>> getNotificationsByEmail(@PathVariable String email) {
        List<Notification> notifications = notificationRepository.findByRecipientEmail(email,"UNREAD");  // Using NotificationRepository
        return ResponseEntity.ok(notifications);
    }

    // Mark notification as read
    @PostMapping("/read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        // Fetch the notification from the database
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id " + id));

        // Update status to READ
        notification.setStatus("READ");
        notificationRepository.save(notification);  // Save the updated notification

        return ResponseEntity.ok("Notification marked as read");
    }




}
