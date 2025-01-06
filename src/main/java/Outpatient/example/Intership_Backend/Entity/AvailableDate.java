package Outpatient.example.Intership_Backend.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate availableFromDate;

    private LocalDate availableEndDate;


    private String amSlotTiming;

    private String pmSlotTiming;


    @ManyToOne
    @JoinColumn(name = "doctor_email", nullable = false)
    @JsonBackReference
    @ToString.Exclude// Prevents serialization of the `doctor` field to avoid circular reference
    private Doctor doctor;
}
