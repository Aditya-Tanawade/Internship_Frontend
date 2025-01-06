package Outpatient.example.Intership_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private String patientName;

    private String mobileNo;

    @Id
    private String email;

    private String bloodGroup;

    private String gender;

    private int age;

    private String address;

}
