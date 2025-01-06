//package Outpatient.example.Intership_Backend.Repository;
//
//import Outpatient.example.Intership_Backend.Entity.Appointment;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
//
//    List<Appointment> findByDoctorEmail(String doctorEmail);
//
//    List<Appointment> findByPatientEmail(String patientEmail);
//
//
//}
package Outpatient.example.Intership_Backend.Repository;

import Outpatient.example.Intership_Backend.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    Optional<Appointment> findById(Long id);  // Get appointment by its ID


    List<Appointment> findByDoctorEmail(String doctorEmail);

    List<Appointment> findByPatientEmail(String patientEmail);


    List<Appointment> findByDoctorEmailAndStatus(String doctorEmail, String status);

    @Query("SELECT a FROM Appointment a WHERE a.doctorEmail = :doctorEmail AND a.status IS NULL")
    List<Appointment> findByDoctorEmailAndStatusIsNull(String doctorEmail);


    void deleteByDoctorEmail(String email);

    void deleteByPatientEmail(String email);



    int countByStatusAndDoctorEmail(String status, String doctorEmail);

    @Query("SELECT COUNT(DISTINCT a.patient) FROM Appointment a WHERE a.doctorEmail = :doctorEmail")
    int countDistinctPatientsByDoctorEmail(@Param("doctorEmail") String doctorEmail);


    int countPatientsByDoctorEmail(String loginEmail);

    int countByStatusIsNullAndDoctorEmail(String doctorEmail);

    int countDistinctPatientsAndStatusIsNullByDoctorEmail(String loginEmail);

    int countByStatusIsNullAndPatientEmail(String loginEmail);

    int countByStatusAndPatientEmail(String completed, String loginEmail);



}