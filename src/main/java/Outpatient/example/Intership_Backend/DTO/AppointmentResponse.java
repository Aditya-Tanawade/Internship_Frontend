package Outpatient.example.Intership_Backend.DTO;

import Outpatient.example.Intership_Backend.Entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Appointment appointment;
    private String checkoutUrl;
}
