package com.example.demo.service;

import com.example.demo.model.Cycle;
import com.example.demo.model.Objective;
import com.example.demo.model.Team;
import com.example.demo.repository.CycleRepository;
import com.example.demo.repository.ObjectiveRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    public Objective create(Objective objective) {
        return objectiveRepository.save(objective);
    }

//---------------------------- Buscar lista de todos os objetivos -------------------------------------
    public List<Objective> findAll() {
        return objectiveRepository.findAll();
    }

//---------------------------- Buscar objetivo por ID -------------------------------------------------
    public Optional<Objective> findById(Long id) {
        return objectiveRepository.findById(id);
    }

//------------------------ Atualiza Objetivo Existente ------------------------------------------------
    public Objective update(Long id, Objective updatedObjective) {
        return objectiveRepository.findById(id).map(objective -> {
            objective.setTitle(updatedObjective.getTitle());
            objective.setDescription(updatedObjective.getDescription());
            objective.setTeam(updatedObjective.getTeam());
            objective.setCycles(updatedObjective.getCycles());
            return objectiveRepository.save(objective);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));
    }

//----------------------- Deleta Objetivo Existente ---------------------------------------------------
    public void delete(Long id) {
        objectiveRepository.deleteById(id);
    }

//------------------------ Cria Objetivo (DTO) --------------------------------------------------------
    // Método para criar Objective a partir do DTO
    public Objective createFromDTO(com.example.demo.DTO.ObjectiveDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title não pode ser nulo ou vazio");
        }

        Team team = teamRepository.findByName(dto.getTeamName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<Cycle> cycles = new HashSet<>();
        if (dto.getCycleIds() != null) {
            for (Long id : dto.getCycleIds()) {
                Cycle cycle = cycleRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cycle não encontrado com ID: " + id));
                cycles.add(cycle);
            }
        }

        Objective objective = new Objective();
        objective.setTitle(dto.getTitle());
        objective.setDescription(dto.getDescription());
        objective.setTeam(team);
        objective.setCycles(cycles);

        return objectiveRepository.save(objective);
    }
}
