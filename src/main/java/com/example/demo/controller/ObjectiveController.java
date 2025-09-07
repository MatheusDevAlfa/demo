package com.example.demo.controller;

import com.example.demo.DTO.ObjectiveDTO;
import com.example.demo.model.Objective;
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
    public List<Objective> getAllObjectives() {
        return objectiveService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Objective> getObjectiveById(@PathVariable Long id) {
        return objectiveService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Objective> createObjective(@RequestBody ObjectiveDTO dto) {
        Objective createdObjective = objectiveService.createFromDTO(dto);
        return ResponseEntity.status(201).body(createdObjective);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Objective> updateObjective(@PathVariable Long id, @RequestBody ObjectiveDTO dto) {
        Objective updatedObjective = objectiveService.updateFromDTO(id, dto);
        return ResponseEntity.ok(updatedObjective);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable Long id) {
        objectiveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
