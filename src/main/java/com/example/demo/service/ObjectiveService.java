package com.example.demo.service;


import com.example.demo.model.Objective;
import com.example.demo.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObjectiveService {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    public Objective create(Objective objective) {
        // Regra 1: o objetivo deve ser associado a um time e um ciclo.
        // O front-end precisa enviar essas informações.
        return objectiveRepository.save(objective);
    }

    public List<Objective> findAll() {
        return objectiveRepository.findAll();
    }

    public Optional<Objective> findById(Long id) {
        return objectiveRepository.findById(id);
    }

    // Objetivo: tornar a atualização mais robusta
    public Objective update(Long id, Objective updatedObjective) {
        return objectiveRepository.findById(id).map(objective -> {
            // Encontrou o objetivo, agora atualiza os dados
            objective.setTitle(updatedObjective.getTitle());
            objective.setDescription(updatedObjective.getDescription());
            objective.setCompleted(updatedObjective.isCompleted());
            // Adicione outras regras que precisar aqui
            return objectiveRepository.save(objective);
        }).orElseThrow(() -> new RuntimeException("Objective not found with ID " + id));
    }

    public void delete(Long id) {
        objectiveRepository.deleteById(id);
    }
}