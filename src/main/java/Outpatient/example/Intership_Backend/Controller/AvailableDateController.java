package Outpatient.example.Intership_Backend.Controller;


import Outpatient.example.Intership_Backend.Entity.AvailableDate;
import Outpatient.example.Intership_Backend.Service.AvailableDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class AvailableDateController {

    @Autowired
    private AvailableDateService availableDateService;


    @GetMapping("/availability")
    public ResponseEntity<AvailableDate> getAvailabilityByDoctor(@RequestParam String email) {
        AvailableDate availableDate = availableDateService.getAvailabilityByDoctorEmail(email);
        if (availableDate != null) {
            return ResponseEntity.ok(availableDate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update availability
    @PutMapping("/availability")
    public ResponseEntity<AvailableDate> updateAvailability(@RequestBody AvailableDate availableDate) {
        try {
            AvailableDate updatedAvailability = availableDateService.updateAvailability(availableDate);
            return ResponseEntity.ok(updatedAvailability);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }




}
