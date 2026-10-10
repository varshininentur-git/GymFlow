package com.gym.management;
import com.gym.management.dto.TrainerRequest;
import com.gym.management.entity.Trainer;
import com.gym.management.exception.NotFoundException;
import com.gym.management.repository.TrainerRepository;
import com.gym.management.service.TrainerService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
class TrainerServiceTests {
 @Test void invalidTrainerFieldsFailBeanValidation(){var validator=jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();var invalid=new TrainerRequest(" ","not-email","","Strength","",null);assertFalse(validator.validate(invalid).isEmpty());}
 @Test void createsUpdatesAndDeactivatesTrainer(){TrainerRepository repo=mock(TrainerRepository.class);TrainerService service=new TrainerService(repo);Trainer trainer=new Trainer();when(repo.save(any())).thenAnswer(i->i.getArgument(0));
  TrainerRequest r=new TrainerRequest("Taylor Coach","taylor@example.com","555-0100","Strength","Weekdays",null);
  assertEquals("Taylor Coach",service.save(null,r).getName());when(repo.findById(1L)).thenReturn(Optional.of(trainer));
  Trainer updated=service.save(1L,new TrainerRequest("Taylor Updated","taylor@example.com","555-0101","Yoga","Weekends",null));assertEquals("Taylor Updated",updated.getName());service.deactivate(1L);assertFalse(trainer.isActive());}
 @Test void missingTrainerIsRejected(){TrainerService service=new TrainerService(mock(TrainerRepository.class));assertThrows(NotFoundException.class,()->service.get(99L));}
}
