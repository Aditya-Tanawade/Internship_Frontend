package Outpatient.example.Intership_Backend.Service;



import Outpatient.example.Intership_Backend.Entity.AvailableDate;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Repository.AvailableDateRepository;
import Outpatient.example.Intership_Backend.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvailableDateService {

    @Autowired
    private AvailableDateRepository availableDateRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorRepository doctorRepository;




//

    public AvailableDate updateAvailability(AvailableDate availableDate) {
        AvailableDate existingSlot = availableDateRepository.findByDoctorEmail(doctorService.getLoginEmail());

        if (existingSlot != null) {
            System.out.println("Updating existing slot: " + existingSlot);

            // Check incoming values
            System.out.println("Incoming availableFromDate: " + availableDate.getAvailableFromDate());
            System.out.println("Incoming availableEndDate: " + availableDate.getAvailableEndDate());

            existingSlot.setAvailableFromDate(availableDate.getAvailableFromDate());
            existingSlot.setAvailableEndDate(availableDate.getAvailableEndDate());
            existingSlot.setAmSlotTiming(availableDate.getAmSlotTiming());
            existingSlot.setPmSlotTiming(availableDate.getPmSlotTiming());

            AvailableDate updatedSlot = availableDateRepository.save(existingSlot);
            System.out.println("Updated slot: " + updatedSlot);
            return updatedSlot;
        } else {
            Doctor doctor = doctorRepository.findByEmail(doctorService.getLoginEmail());
            availableDate.setDoctor(doctor);
            return availableDateRepository.save(availableDate);
        }




    }

    public AvailableDate getAvailabilityByDoctorEmail(String email) {
        return availableDateRepository.findByDoctorEmail(email);

    }
}