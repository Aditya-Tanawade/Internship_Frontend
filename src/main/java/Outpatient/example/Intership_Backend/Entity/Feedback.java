package Outpatient.example.Intership_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;  // Name of the patient
    private String doctorName;   // Name of the doctor

    private String comment;

    @Column(nullable = false)
    private int rating;

    private int likes; // Add a field to track likes

}
