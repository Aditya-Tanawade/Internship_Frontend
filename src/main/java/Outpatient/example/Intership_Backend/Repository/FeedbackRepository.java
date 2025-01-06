package Outpatient.example.Intership_Backend.Repository;

import Outpatient.example.Intership_Backend.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
