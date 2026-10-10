package com.gym.management.service;
import com.gym.management.dto.TrainerRequest;
import com.gym.management.entity.Trainer;
import com.gym.management.exception.*;
import com.gym.management.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class TrainerService {
 private final TrainerRepository repo; public TrainerService(TrainerRepository repo){this.repo=repo;}
 @Transactional(readOnly=true) public List<Trainer> list(String search,Boolean active){
  List<Trainer> rows=search==null||search.isBlank()?repo.findAll():repo.findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(search,search);
  return active==null?rows:rows.stream().filter(t->t.isActive()==active).toList();
 }
 @Transactional(readOnly=true) public Trainer get(Long id){return repo.findById(id).orElseThrow(()->new NotFoundException("Trainer not found"));}
 public Trainer save(Long id,TrainerRequest r){Trainer t=id==null?new Trainer():get(id); if(id==null?repo.existsByEmailIgnoreCase(r.email()):repo.existsByEmailIgnoreCaseAndIdNot(r.email(),id))throw new ConflictException("A trainer with this email already exists");
 t.setName(r.name());t.setEmail(r.email());t.setPhone(r.phone());t.setSpecialization(r.specialization());t.setAvailability(r.availability());if(r.active()!=null)t.setActive(r.active());return repo.save(t);}
 public void deactivate(Long id){get(id).setActive(false);}
}
