package com.example.demo.service;

import com.example.demo.DTO.ObjectiveDTO;
import com.example.demo.model.Cycle;
import com.example.demo.model.Objective;
import com.example.demo.model.Team;
import com.example.demo.repository.CycleRepository;
import com.example.demo.repository.ObjectiveRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ObjectiveService {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CycleRepository cycleRepository;

    //------------------------ Buscar todos os objetivos ativos ------------------------------
    public List<Objective> findAll() {
        return objectiveRepository.findAll()
                .stream()
                .filter(Objective::isActive)
                .toList();
    }

    //------------------------ Buscar objetivo ativo por ID ---------------------------------
    public Optional<Objective> findById(Long id) {
        return objectiveRepository.findById(id)
                .filter(Objective::isActive);
    }

    //------------------------ Criar objetivo a partir de DTO --------------------------------
    public Objective createFromDTO(ObjectiveDTO dto) {
        validateDTO(dto); // Valida título, team e cycles

        Team team = teamRepository.findByName(dto.getTeamName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<Cycle> cycles = fetchValidCycles(dto.getCycleIds());

        Objective objective = new Objective();
        objective.setTitle(dto.getTitle());
        objective.setDescription(dto.getDescription());
        objective.setTeam(team);
        objective.setCycles(cycles);
        objective.setActive(true);

        return objectiveRepository.save(objective);
    }

    //------------------------ Atualizar objetivo a partir de DTO ---------------------------
    public Objective updateFromDTO(Long id, ObjectiveDTO dto) {
        Objective existingObjective = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));

        if (!existingObjective.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Objetivo não está ativo e não pode ser alterado");
        }

        validateDTO(dto);

        Team team = teamRepository.findByName(dto.getTeamName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<Cycle> cycles = fetchValidCycles(dto.getCycleIds());

        existingObjective.setTitle(dto.getTitle());
        existingObjective.setDescription(dto.getDescription());
        existingObjective.setTeam(team);
        existingObjective.setCycles(cycles);

        return objectiveRepository.save(existingObjective);
    }

    //------------------------ Deletar objetivo (soft delete) --------------------------------
    public Objective delete(Long id) {
        Objective objective = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));
        objective.setActive(false);
        return objectiveRepository.save(objective);
    }

    //------------------------ Métodos auxiliares -------------------------------------------
    private void validateDTO(ObjectiveDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Título não pode ser nulo ou vazio");
        }
        if (dto.getTeamName() == null || dto.getTeamName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Team deve ser informado");
        }
        if (dto.getCycleIds() == null || dto.getCycleIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deve ser selecionado pelo menos um ciclo");
        }
    }

    private Set<Cycle> fetchValidCycles(Set<Long> cycleIds) {
        Set<Cycle> cycles = new HashSet<>();
        for (Long id : cycleIds) {
            Cycle cycle = cycleRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cycle não encontrado com ID: " + id));
            if (!cycle.isActive()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cycle com ID " + id + " não está ativo");
            }
            cycles.add(cycle);
        }
        return cycles;
    }
}
