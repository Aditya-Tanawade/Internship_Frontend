package Outpatient.example.Intership_Backend.Service;

import Outpatient.example.Intership_Backend.DTO.PrescriptionDTO;
import Outpatient.example.Intership_Backend.Entity.Appointment;
import Outpatient.example.Intership_Backend.Entity.Doctor;
import Outpatient.example.Intership_Backend.Entity.Patient;
import Outpatient.example.Intership_Backend.Entity.Prescription;
import Outpatient.example.Intership_Backend.Repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
//public class PrescriptionService {
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @Autowired
//    private PatientService patientService;
//
//    private final PrescriptionRepository prescriptionRepository;
//
//    @Autowired
//    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
//        this.prescriptionRepository = prescriptionRepository;
//    }
//
//    public Prescription savePrescription(Prescription prescription) {
//
//        prescription.setDoctor(doctorService.getDoctorProfile());
//
//        return prescriptionRepository.save(prescription);
//    }
//
//    public Prescription getPrescriptionById(int id) {
//        return prescriptionRepository.findById(id).orElse(null);
//    }
//
//    public Prescription getPrescriptionByEmail(String email) {
//        Patient p=patientService.getPatientProfile();
//
//        return prescriptionRepository.findByPatientEmail(patientService.getLoginEmail());
//    }
//
////    public PrescriptionDTO getPrescriptionForLoggedInUser() {
////        Prescription prescription = prescriptionRepository.findByPatientEmail(patientService.getLoginEmail());
////        Patient p=patientService.getPatientProfile();
////        if (prescription != null) {
////            PrescriptionDTO dto = new PrescriptionDTO();
////
////
////            dto.setDoctorName(prescription.getDoctor().getDoctorName());
////            dto.setDoctorEmail(prescription.getDoctor().getEmail());
////            dto.setDoctorSpeciality(prescription.getDoctor().getSpeciality());
////            dto.setDoctorCharge(prescription.getDoctor().getChargedPerVisit());
////
////            dto.setPatientEmail(prescription.getPatientEmail());
////            dto.setPatientName(p.getPatientName());
////            dto.setPatientAge(p.getAge());
////            dto.setPatientBloodGroup(p.getBloodGroup());
////            dto.setPatientGender(p.getGender());
////
////            dto.setAdvice(prescription.getAdvice());
////            dto.setReason(prescription.getReason());
////            return dto;
////        }
////        return null;
////    }
//
//
//
//    public PrescriptionDTO getPrescriptionForLoggedInUser() {
//        Prescription prescription = prescriptionRepository.findByPatientEmail(patientService.getLoginEmail());
//        if (prescription != null) {
//            Doctor doctor = prescription.getDoctor();
//            Patient patient = patientService.getPatientProfile();
//
//            if (doctor == null || patient == null) {
//                throw new RuntimeException("Doctor or Patient details are missing!");
//            }
//
//            PrescriptionDTO dto = new PrescriptionDTO();
//            dto.setDoctorName(doctor.getDoctorName());
//            dto.setDoctorEmail(doctor.getEmail());
//            dto.setDoctorSpeciality(doctor.getSpeciality());
//            dto.setDoctorCharge(doctor.getChargedPerVisit());
//
//            dto.setPatientEmail(prescription.getPatientEmail());
//            dto.setPatientName(patient.getPatientName());
//            dto.setPatientAge(patient.getAge());
//            dto.setPatientBloodGroup(patient.getBloodGroup());
//            dto.setPatientGender(patient.getGender());
//
//            dto.setAdvice(prescription.getAdvice());
//            dto.setReason(prescription.getReason());
//            return dto;
//        }
//        return null;
//    }

//}



@Service
public class PrescriptionService {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService; // Assuming there's a service to fetch appointment details

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public Prescription savePrescription(Prescription prescription, int appointmentId) {
        // Fetch appointment by ID
        Optional<Appointment> appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Appointment not found for ID: " + appointmentId);
        }

        prescription.setAppointment(appointment.get());
        prescription.setDoctor(doctorService.getDoctorProfile());

        return prescriptionRepository.save(prescription);
    }

    public Prescription getPrescriptionByAppointmentId(Long appointmentId) {
        // Retrieve prescription by appointment ID
        return prescriptionRepository.findByAppointmentId(appointmentId).orElse(null);
    }

    public PrescriptionDTO getPrescriptionForLoggedInUser(Long appointmentId) {
        Prescription prescription = getPrescriptionByAppointmentId(appointmentId);
        if (prescription != null) {
            Doctor doctor = prescription.getDoctor();
            Patient patient = patientService.getPatientProfile();

            if (doctor == null || patient == null) {
                throw new RuntimeException("Doctor or Patient details are missing!");
            }

            PrescriptionDTO dto = new PrescriptionDTO();
            dto.setDoctorName(doctor.getDoctorName());
            dto.setDoctorEmail(doctor.getEmail());
            dto.setDoctorSpeciality(doctor.getSpeciality());
            dto.setDoctorCharge(doctor.getChargedPerVisit());

            dto.setPatientEmail(prescription.getPatientEmail());
            dto.setPatientName(patient.getPatientName());
            dto.setPatientAge(patient.getAge());
            dto.setPatientBloodGroup(patient.getBloodGroup());
            dto.setPatientGender(patient.getGender());

            dto.setAdvice(prescription.getAdvice());
            dto.setReason(prescription.getReason());
            return dto;
        }
        return null;
    }
}
