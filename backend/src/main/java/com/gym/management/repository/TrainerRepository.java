package com.gym.management.repository;
import com.gym.management.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TrainerRepository extends JpaRepository<Trainer,Long>{
 List<Trainer> findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(String name,String specialization);
 List<Trainer> findByActive(boolean active);
 boolean existsByEmailIgnoreCase(String email);
 boolean existsByEmailIgnoreCaseAndIdNot(String email,Long id);
}
