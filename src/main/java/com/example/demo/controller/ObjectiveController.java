package com.example.demo.controller;

import com.example.demo.model.Cycle;
import com.example.demo.model.Objective;
import com.example.demo.model.Team;
import com.example.demo.repository.CycleRepository;
import com.example.demo.repository.ObjectiveRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/objectives")
public class ObjectiveController {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CycleRepository cycleRepository;

    // Construtor para injeção de dependência (boa prática)
    public ObjectiveController(ObjectiveRepository objectiveRepository, TeamRepository teamRepository, CycleRepository cycleRepository) {
        this.objectiveRepository = objectiveRepository;
        this.teamRepository = teamRepository;
        this.cycleRepository = cycleRepository;
    }

    // Endpoint para buscar todos os objetivos
    @GetMapping
    public List<Objective> getAllObjectives() {
        return objectiveRepository.findAll();
    }

    // Endpoint para buscar um objetivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Objective> getObjectiveById(@PathVariable Long id) {
        Objective objective = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objetivo não encontrado com o ID: " + id));
        return ResponseEntity.ok(objective);
    }

    // Endpoint para salvar um novo objetivo
    @PostMapping
    public ResponseEntity<Objective> createObjective(@RequestBody Objective objective) {
        // Trata as entidades Team e Cycle
        Team team = teamRepository.findByName(objective.getTeam().getName())
                .orElseGet(() -> teamRepository.save(objective.getTeam()));

        Cycle cycle = cycleRepository.findByName(objective.getCycle().getName())
                .orElseGet(() -> cycleRepository.save(objective.getCycle()));

        objective.setTeam(team);
        objective.setCycle(cycle);

        Objective newObjective = objectiveRepository.save(objective);
        return new ResponseEntity<>(newObjective, HttpStatus.CREATED);
    }

    // Endpoint para atualizar um objetivo
    @PutMapping("/{id}")
    public ResponseEntity<Objective> updateObjective(@PathVariable Long id, @RequestBody Objective objectiveDetails) {
        Objective existingObjective = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objetivo não encontrado com o ID: " + id));

        // Atualiza as informações do objetivo
        existingObjective.setTitle(objectiveDetails.getTitle());
        existingObjective.setDescription(objectiveDetails.getDescription());

        // Trata as entidades Team e Cycle para a atualização
        Team team = teamRepository.findByName(objectiveDetails.getTeam().getName())
                .orElseGet(() -> teamRepository.save(objectiveDetails.getTeam()));

        Cycle cycle = cycleRepository.findByName(objectiveDetails.getCycle().getName())
                .orElseGet(() -> cycleRepository.save(objectiveDetails.getCycle()));

        existingObjective.setTeam(team);
        existingObjective.setCycle(cycle);

        Objective updatedObjective = objectiveRepository.save(existingObjective);
        return ResponseEntity.ok(updatedObjective);
    }

    // Endpoint para deletar um objetivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObjective(@PathVariable Long id) {
        Objective objective = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objetivo não encontrado com o ID: " + id));

        objectiveRepository.delete(objective);
        return ResponseEntity.noContent().build();
    }
}
