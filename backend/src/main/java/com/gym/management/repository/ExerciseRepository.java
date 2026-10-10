package com.gym.management.repository;
import com.gym.management.entity.Exercise;import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;
public interface ExerciseRepository extends JpaRepository<Exercise,Long>{List<Exercise> findByActiveTrueOrderByMuscleGroupAscNameAsc();boolean existsByNameIgnoreCase(String name);}
