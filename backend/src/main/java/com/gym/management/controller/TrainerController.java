package com.gym.management.controller;
import com.gym.management.dto.TrainerRequest;
import com.gym.management.entity.Trainer;
import com.gym.management.service.TrainerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/trainers")
public class TrainerController {
 private final TrainerService service; public TrainerController(TrainerService service){this.service=service;}
 @GetMapping public List<Trainer> list(@RequestParam(required=false)String search,@RequestParam(required=false)Boolean active){return service.list(search,active);}
 @GetMapping("/{id}") public Trainer get(@PathVariable Long id){return service.get(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Trainer create(@Valid @RequestBody TrainerRequest r){return service.save(null,r);}
 @PutMapping("/{id}") public Trainer update(@PathVariable Long id,@Valid @RequestBody TrainerRequest r){return service.save(id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deactivate(@PathVariable Long id){service.deactivate(id);}
}
