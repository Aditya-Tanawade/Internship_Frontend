package Outpatient.example.Intership_Backend.DTO;

import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Patient;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointmentDto {

    @ManyToOne
    @JoinColumn(name = "doctor_email")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_email")
    private Patient patient;

    private LocalDate appointmentDate;

    private String reason;
}
