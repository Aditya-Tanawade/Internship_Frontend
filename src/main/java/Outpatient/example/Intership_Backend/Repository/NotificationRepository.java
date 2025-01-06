package Outpatient.example.Intership_Backend.Repository;

import Outpatient.example.Intership_Backend.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
   //new // Custom method to find notifications by recipient's email

    @Query(value = "SELECT * FROM notification WHERE recipient_email = :recipientEmail AND status = :status", nativeQuery = true)
    List<Notification> findByRecipientEmail(String recipientEmail, String status);
}

