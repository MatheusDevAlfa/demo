package com.example.demo.service;

import com.example.demo.DTO.ObjectiveDTO;
import com.example.demo.domain.entity.Cycle;
import com.example.demo.domain.entity.ObjectiveEntity;
import com.example.demo.domain.entity.Team;
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
    public List<ObjectiveEntity> findAll() {
        return objectiveRepository.findAll()
                .stream()
                .filter(ObjectiveEntity::isActive)
                .toList();
    }

    //------------------------ Buscar objetivo ativo por ID ---------------------------------
    public Optional<ObjectiveEntity> findById(Long id) {
        return objectiveRepository.findById(id)
                .filter(ObjectiveEntity::isActive);
    }

    //------------------------ Criar objetivo a partir de DTO --------------------------------
    public ObjectiveEntity createFromDTO(ObjectiveDTO dto) {
        validateDTO(dto); // Valida título, team e cycles

        Team team = teamRepository.findByName(dto.getTeamName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<Cycle> cycles = fetchValidCycles(dto.getCycleIds());

        ObjectiveEntity objectiveEntity = new ObjectiveEntity();
        objectiveEntity.setTitle(dto.getTitle());
        objectiveEntity.setDescription(dto.getDescription());
        objectiveEntity.setTeam(team);
        objectiveEntity.setCycles(cycles);
        objectiveEntity.setActive(true);

        return objectiveRepository.save(objectiveEntity);
    }

    //------------------------ Atualizar objetivo a partir de DTO ---------------------------
    public ObjectiveEntity updateFromDTO(Long id, ObjectiveDTO dto) {
        ObjectiveEntity existingObjectiveEntity = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));

        if (!existingObjectiveEntity.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Objetivo não está ativo e não pode ser alterado");
        }

        validateDTO(dto);

        Team team = teamRepository.findByName(dto.getTeamName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<Cycle> cycles = fetchValidCycles(dto.getCycleIds());

        existingObjectiveEntity.setTitle(dto.getTitle());
        existingObjectiveEntity.setDescription(dto.getDescription());
        existingObjectiveEntity.setTeam(team);
        existingObjectiveEntity.setCycles(cycles);

        return objectiveRepository.save(existingObjectiveEntity);
    }

    //------------------------ Deletar objetivo (soft delete) --------------------------------
    public ObjectiveEntity delete(Long id) {
        ObjectiveEntity objectiveEntity = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));
        objectiveEntity.setActive(false);
        return objectiveRepository.save(objectiveEntity);
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
