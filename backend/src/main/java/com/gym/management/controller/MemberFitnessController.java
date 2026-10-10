package com.gym.management.controller;
import com.gym.management.dto.MemberFitnessDtos.*;import com.gym.management.service.MemberFitnessService;import jakarta.validation.Valid;import org.springframework.format.annotation.DateTimeFormat;import org.springframework.http.HttpStatus;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.time.*;import java.util.*;
@RestController @RequestMapping("/api/member")
public class MemberFitnessController{
 private final MemberFitnessService service;public MemberFitnessController(MemberFitnessService service){this.service=service;}
 @GetMapping("/fitness/summary")public FitnessSummary summary(Authentication a){return service.summary(a.getName());}
 @GetMapping("/measurements")public List<MeasurementDto> measurements(Authentication a,@RequestParam(required=false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate from,@RequestParam(required=false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate to){return service.measurements(a.getName(),from,to);}
 @PostMapping("/measurements")@ResponseStatus(HttpStatus.CREATED)public MeasurementDto addMeasurement(Authentication a,@Valid @RequestBody MeasurementRequest r){return service.saveMeasurement(a.getName(),null,r);}
 @PutMapping("/measurements/{id}")public MeasurementDto updateMeasurement(Authentication a,@PathVariable Long id,@Valid @RequestBody MeasurementRequest r){return service.saveMeasurement(a.getName(),id,r);}
 @DeleteMapping("/measurements/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void deleteMeasurement(Authentication a,@PathVariable Long id){service.deleteMeasurement(a.getName(),id);}
 @GetMapping("/goals")public List<GoalDto> goals(Authentication a){return service.goals(a.getName());}
 @PostMapping("/goals")@ResponseStatus(HttpStatus.CREATED)public GoalDto addGoal(Authentication a,@Valid @RequestBody GoalRequest r){return service.saveGoal(a.getName(),null,r);}
 @PutMapping("/goals/{id}")public GoalDto updateGoal(Authentication a,@PathVariable Long id,@Valid @RequestBody GoalRequest r){return service.saveGoal(a.getName(),id,r);}
 @PostMapping("/goals/{id}/complete")public GoalDto completeGoal(Authentication a,@PathVariable Long id){return service.completeGoal(a.getName(),id);}
 @DeleteMapping("/goals/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void deleteGoal(Authentication a,@PathVariable Long id){service.deleteGoal(a.getName(),id);}
 @GetMapping("/exercises")public List<ExerciseDto> exercises(){return service.exerciseCatalog();}
 @GetMapping("/workouts/plans")public List<WorkoutPlanDto> plans(Authentication a){return service.plans(a.getName());}
 @PostMapping("/workouts/plans")@ResponseStatus(HttpStatus.CREATED)public WorkoutPlanDto addPlan(Authentication a,@Valid @RequestBody WorkoutPlanRequest r){return service.savePlan(a.getName(),null,r);}
 @PutMapping("/workouts/plans/{id}")public WorkoutPlanDto updatePlan(Authentication a,@PathVariable Long id,@Valid @RequestBody WorkoutPlanRequest r){return service.savePlan(a.getName(),id,r);}
 @DeleteMapping("/workouts/plans/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void deletePlan(Authentication a,@PathVariable Long id){service.deletePlan(a.getName(),id);}
 @GetMapping("/workouts/sessions")public List<WorkoutSessionDto> history(Authentication a){return service.workoutHistory(a.getName());}
 @GetMapping("/workouts/sessions/active")public WorkoutSessionDto activeWorkout(Authentication a){return service.activeWorkout(a.getName());}
 @PostMapping("/workouts/sessions/start")@ResponseStatus(HttpStatus.CREATED)public WorkoutSessionDto startWorkout(Authentication a,@Valid @RequestBody StartWorkoutRequest r){return service.startWorkout(a.getName(),r);}
 @PostMapping("/workouts/sessions/{id}/complete")public WorkoutSessionDto finishWorkout(Authentication a,@PathVariable Long id,@Valid @RequestBody WorkoutCompletionRequest r){return service.finishWorkout(a.getName(),id,r);}
 @PostMapping("/workouts/sessions")@ResponseStatus(HttpStatus.CREATED)public WorkoutSessionDto completeWorkout(Authentication a,@Valid @RequestBody WorkoutSessionRequest r){return service.completeWorkout(a.getName(),r);}
 @GetMapping("/attendance-goal")public AttendanceGoalDto attendanceGoal(Authentication a){return service.attendanceGoal(a.getName());}
 @PutMapping("/attendance-goal")public AttendanceGoalDto saveAttendanceGoal(Authentication a,@Valid @RequestBody AttendanceGoalRequest r){return service.saveAttendanceGoal(a.getName(),r);}
 @GetMapping("/achievements")public List<BadgeDto> achievements(Authentication a){return service.achievements(a.getName());}
 @GetMapping("/nutrition")public NutritionSummary nutrition(Authentication a,@RequestParam(required=false)@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)LocalDate date){return service.nutrition(a.getName(),date);}
 @PostMapping("/nutrition/meals")@ResponseStatus(HttpStatus.CREATED)public MealDto addMeal(Authentication a,@Valid @RequestBody MealRequest r){return service.saveMeal(a.getName(),null,r);}
 @PutMapping("/nutrition/meals/{id}")public MealDto updateMeal(Authentication a,@PathVariable Long id,@Valid @RequestBody MealRequest r){return service.saveMeal(a.getName(),id,r);}
 @DeleteMapping("/nutrition/meals/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void deleteMeal(Authentication a,@PathVariable Long id){service.deleteMeal(a.getName(),id);}
 @PutMapping("/nutrition/settings")public WellnessDto saveSettings(Authentication a,@Valid @RequestBody WellnessRequest r){return service.saveSettings(a.getName(),r);}
 @GetMapping("/nutrition/settings")public WellnessDto settings(Authentication a){return service.wellnessSettings(a.getName());}
 @PostMapping("/nutrition/water")@ResponseStatus(HttpStatus.CREATED)public WaterDto addWater(Authentication a,@Valid @RequestBody WaterRequest r){return service.addWater(a.getName(),r);}
 @DeleteMapping("/nutrition/water/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void deleteWater(Authentication a,@PathVariable Long id){service.deleteWater(a.getName(),id);}
}
