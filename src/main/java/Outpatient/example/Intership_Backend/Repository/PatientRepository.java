package Outpatient.example.Intership_Backend.Repository;

import Outpatient.example.Intership_Backend.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,String > {

    Patient findByEmail(String email);

    Patient deleteByEmail(String email);


    boolean existsByEmail(String email);
}
