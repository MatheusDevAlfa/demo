package com.example.okr.Controller;

import com.example.okr.DTO.ObjetivoDTO;
import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.service.ObjectiveService;
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
    public List<ObjetivoEntity> getAllObjectives() {
        return objectiveService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjetivoEntity> getObjectiveById(@PathVariable Long id) {
        return objectiveService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ObjetivoEntity> createObjective(@RequestBody ObjetivoDTO dto) {
        ObjetivoEntity createdObjetivoEntity = objectiveService.createFromDTO(dto);
        return ResponseEntity.status(201).body(createdObjetivoEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjetivoEntity> updateObjective(@PathVariable Long id, @RequestBody ObjetivoDTO dto) {
        ObjetivoEntity updatedObjetivoEntity = objectiveService.updateFromDTO(id, dto);
        return ResponseEntity.ok(updatedObjetivoEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable Long id) {
        objectiveService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
