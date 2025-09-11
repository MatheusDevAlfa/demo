package com.example.demo.Controller;

import com.example.demo.DTO.ObjectiveDTO;
import com.example.demo.domain.entity.ObjectiveEntity;
import com.example.demo.service.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objectives")
@CrossOrigin(origins = "*")
public class ObjectiveController {

    @Autowired
    private ObjectiveService objectiveService;

    @GetMapping
    public List<ObjectiveEntity> getAllObjectives() {
        return objectiveService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectiveEntity> getObjectiveById(@PathVariable Long id) {
        return objectiveService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ObjectiveEntity> createObjective(@RequestBody ObjectiveDTO dto) {
        ObjectiveEntity createdObjectiveEntity = objectiveService.createFromDTO(dto);
        return ResponseEntity.status(201).body(createdObjectiveEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjectiveEntity> updateObjective(@PathVariable Long id, @RequestBody ObjectiveDTO dto) {
        ObjectiveEntity updatedObjectiveEntity = objectiveService.updateFromDTO(id, dto);
        return ResponseEntity.ok(updatedObjectiveEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable Long id) {
        objectiveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
