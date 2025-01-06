package Outpatient.example.Intership_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_email")
    private String recipientEmail; // Patient email address

    private String subject;

    @Column(length = 500)
    private String message;

    private String status; // e.g., PENDING, SENT, FAILED

    private LocalDateTime timestamp;
}
