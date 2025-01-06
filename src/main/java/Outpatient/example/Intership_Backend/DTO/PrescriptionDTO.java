package Outpatient.example.Intership_Backend.DTO;

import lombok.Data;

@Data
public class PrescriptionDTO {

    private String doctorName;
    private String doctorEmail;
    private String doctorSpeciality;
    private double doctorCharge;
    private String patientName;
    private String patientEmail;
    private String patientGender;
    private int patientAge;
    private String patientBloodGroup;
    private String reason;
    private String advice;
}

