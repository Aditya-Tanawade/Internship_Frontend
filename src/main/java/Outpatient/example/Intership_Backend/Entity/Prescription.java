package Outpatient.example.Intership_Backend.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Prescription
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String patientEmail;

    @ManyToOne
    @JoinColumn(name = "doctor_email")
    @JsonBackReference
    private Doctor doctor;


    private String advice;

    private String reason;


}
