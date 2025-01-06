package Outpatient.example.Intership_Backend.Repository;

import Outpatient.example.Intership_Backend.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

