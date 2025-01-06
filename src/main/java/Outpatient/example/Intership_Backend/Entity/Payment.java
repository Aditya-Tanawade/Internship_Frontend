package Outpatient.example.Intership_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private Long appointmentId;

    private String doctorEmail;

    @ManyToOne
    @JoinColumn(name = "patient_email")
    private Patient patient;

}
