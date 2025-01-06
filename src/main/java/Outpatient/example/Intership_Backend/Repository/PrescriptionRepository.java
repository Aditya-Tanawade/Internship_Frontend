package Outpatient.example.Intership_Backend.Repository;


import Outpatient.example.Intership_Backend.Entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer>
{
    Optional<Prescription> findByAppointmentId(Long appointmentId);

}
