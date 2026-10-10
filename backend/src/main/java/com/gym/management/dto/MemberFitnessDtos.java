package com.gym.management.dto;

import jakarta.validation.Valid;import jakarta.validation.constraints.*;import java.math.BigDecimal;import java.time.*;import java.util.*;

public final class MemberFitnessDtos {
 private MemberFitnessDtos(){}
 public record MeasurementRequest(@NotNull LocalDate measurementDate,@DecimalMin("20.0") @DecimalMax("350.0") BigDecimal weightKg,@DecimalMin("20.0") @DecimalMax("250.0") BigDecimal waistCm,@DecimalMin("20.0") @DecimalMax("250.0") BigDecimal chestCm,@DecimalMin("20.0") @DecimalMax("250.0") BigDecimal hipsCm,@DecimalMin("5.0") @DecimalMax("120.0") BigDecimal armsCm,@DecimalMin("10.0") @DecimalMax("180.0") BigDecimal thighsCm){}
 public record MeasurementDto(Long id,LocalDate measurementDate,BigDecimal weightKg,BigDecimal waistCm,BigDecimal chestCm,BigDecimal hipsCm,BigDecimal armsCm,BigDecimal thighsCm){}
 public record GoalRequest(@NotBlank @Size(max=80) String title,@Size(max=1000) String description,@NotBlank @Pattern(regexp="WEIGHT_LOSS|WEIGHT_GAIN|MEASUREMENT|GENERAL") String goalType,@DecimalMin("0.1") @DecimalMax("1000") BigDecimal targetValue,@Size(max=24) String unit,LocalDate targetDate){}
 public record GoalDto(Long id,String title,String description,String goalType,BigDecimal targetValue,String unit,LocalDate targetDate,String status,LocalDateTime completedAt,BigDecimal currentValue,Integer progressPercent){}
 public record PlanExerciseRequest(@NotNull Long exerciseId,@Min(1) @Max(50) int targetSets,@Min(1) @Max(500) int targetReps,@Min(0) @Max(3600) int restSeconds,@Min(0) int position){}
 public record WorkoutPlanRequest(@NotBlank @Size(max=100) String name,@Size(max=1000) String description,@NotEmpty @Size(max=40) List<@Valid PlanExerciseRequest> exercises){}
 public record PlanExerciseDto(Long exerciseId,String exerciseName,String muscleGroup,String category,String equipment,String instructions,int position,int targetSets,int targetReps,int restSeconds){}
 public record ExerciseDto(Long id,String name,String muscleGroup,String category,String equipment,String instructions){}
 public record WorkoutPlanDto(Long id,String name,String description,List<PlanExerciseDto> exercises){}
 public record SessionExerciseRequest(@NotNull Long exerciseId,@Min(1) @Max(100) int completedSets,@Min(1) @Max(500) int repsPerSet,@DecimalMin("0.0") @DecimalMax("1000.0") BigDecimal weightKg){}
 public record WorkoutSessionRequest(@NotBlank @Size(max=80) String idempotencyKey,Long planId,@Size(max=100) String workoutName,LocalDateTime startedAt,@NotNull @Min(1) @Max(1440) Integer durationMinutes,@NotEmpty @Size(max=40) List<@Valid SessionExerciseRequest> exercises){}
 public record SessionExerciseDto(String exerciseName,int completedSets,int repsPerSet,BigDecimal weightKg){}
 public record StartWorkoutRequest(@NotBlank @Size(max=80) String idempotencyKey,Long planId,@Size(max=100) String workoutName,LocalDateTime startedAt){}
 public record WorkoutCompletionRequest(@NotNull @Min(1) @Max(1440) Integer durationMinutes,@NotEmpty @Size(max=40) List<@Valid SessionExerciseRequest> exercises){}
 public record WorkoutSessionDto(Long id,Long planId,String workoutName,LocalDateTime startedAt,LocalDateTime completedAt,Integer durationMinutes,List<SessionExerciseDto> exercises){}
 public record AttendanceGoalRequest(@NotBlank @Pattern(regexp="WEEKLY|MONTHLY") String period,@Min(1) @Max(31) int targetVisits){}
 public record AttendanceGoalDto(String period,int targetVisits,int visitsThisPeriod,int remainingVisits,int progressPercent,LocalDate periodStarts,LocalDate periodEnds){}
 public record BadgeDto(String code,String name,String description,boolean earned,LocalDate earnedDate,int current,int threshold){}
 public record MealRequest(@NotBlank @Size(max=100) String name,@NotBlank @Pattern(regexp="BREAKFAST|LUNCH|DINNER|SNACK") String category,@NotNull LocalDateTime occurredAt,@NotNull @DecimalMin("0.0") @DecimalMax("10000") BigDecimal calories,@DecimalMin("0.0") @DecimalMax("1000") BigDecimal proteinGrams,@DecimalMin("0.0") @DecimalMax("1000") BigDecimal carbohydrateGrams,@DecimalMin("0.0") @DecimalMax("1000") BigDecimal fatGrams,@Size(max=500) String notes){}
 public record MealDto(Long id,String name,String category,LocalDateTime occurredAt,BigDecimal calories,BigDecimal proteinGrams,BigDecimal carbohydrateGrams,BigDecimal fatGrams,String notes){}
 public record WaterRequest(@Min(1) @Max(5000) int amountMl,@NotNull LocalDateTime occurredAt,@NotBlank @Size(max=80) String idempotencyKey){}
 public record WaterDto(Long id,int amountMl,LocalDateTime occurredAt){}
 public record WellnessRequest(@Min(100) @Max(10000) Integer waterTargetMl,@DecimalMin("0.0") @DecimalMax("10000") BigDecimal calorieTarget,@DecimalMin("0.0") @DecimalMax("1000") BigDecimal proteinTargetGrams,@DecimalMin("0.0") @DecimalMax("1000") BigDecimal carbohydrateTargetGrams,@DecimalMin("0.0") @DecimalMax("1000") BigDecimal fatTargetGrams){}
 public record NutritionSummary(LocalDate date,List<MealDto> meals,BigDecimal calories,BigDecimal proteinGrams,BigDecimal carbohydrateGrams,BigDecimal fatGrams,int mealCount,BigDecimal calorieTarget,BigDecimal proteinTargetGrams,BigDecimal carbohydrateTargetGrams,BigDecimal fatTargetGrams,List<WaterDto> waterEntries,int waterTotalMl,Integer waterTargetMl){}
 public record NotificationDto(Long id,String category,String title,String message,LocalDateTime createdAt,boolean read,Long announcementId){}
 public record AnnouncementRequest(@NotBlank @Size(max=140) String title,@NotBlank @Size(max=3000) String message,@NotBlank @Pattern(regexp="ALL|MEMBERS") String audience,@NotNull LocalDateTime startsAt,LocalDateTime endsAt,boolean active){}
 public record AnnouncementDto(Long id,String title,String message,String audience,LocalDateTime startsAt,LocalDateTime endsAt,boolean active,LocalDateTime createdAt){}
 public record WellnessDto(Integer waterTargetMl,BigDecimal calorieTarget,BigDecimal proteinTargetGrams,BigDecimal carbohydrateTargetGrams,BigDecimal fatTargetGrams){}
 public record FitnessSummary(MeasurementDto latestMeasurement,BigDecimal weightChangeKg,long activeGoals,long completedWorkouts,long unreadNotifications,AttendanceGoalDto attendanceGoal,int currentAttendanceStreak,int longestAttendanceStreak,List<WorkoutSessionDto> recentWorkouts,BigDecimal caloriesToday,BigDecimal calorieTarget,int waterTotalMl,Integer waterTargetMl){}
}
