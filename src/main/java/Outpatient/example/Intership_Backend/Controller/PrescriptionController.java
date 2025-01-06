package Outpatient.example.Intership_Backend.Controller;

import Outpatient.example.Intership_Backend.DTO.PrescriptionDTO;
import Outpatient.example.Intership_Backend.Entity.Prescription;
import Outpatient.example.Intership_Backend.Service.PatientService;
import Outpatient.example.Intership_Backend.Service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    private PatientService patientService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/{appointmentId}")
    public ResponseEntity<Prescription> addPrescription(@RequestBody Prescription prescription, @PathVariable int appointmentId) {
        Prescription savedPrescription = prescriptionService.savePrescription(prescription, appointmentId);
        return new ResponseEntity<>(savedPrescription, HttpStatus.CREATED);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<PrescriptionDTO> getPrescription(@PathVariable Long appointmentId) {
        PrescriptionDTO prescriptionDTO = prescriptionService.getPrescriptionForLoggedInUser(appointmentId);
        if (prescriptionDTO != null) {
            return new ResponseEntity<>(prescriptionDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
