package com.example.demo.domain.model;

import java.util.HashSet;
import java.util.Set;

public class ObjectiveModel {

    private Long id;
    private String title;
    private String description;
    private boolean active;
    private Set<CycleModel> cycles = new HashSet<>();
    private TeamModel team;

    public ObjectiveModel(Long id, String title, String description, boolean active, TeamModel team) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("O título do objetivo é obrigatório");
        }
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.team = team;
    }

    // 🔹 Regra de negócio: ativar objetivo
    public void activate() {
        if (this.active) {
            throw new IllegalStateException("O objetivo já está ativo");
        }
        this.active = true;
    }

    // 🔹 Regra de negócio: desativar objetivo
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("O objetivo já está inativo");
        }
        this.active = false;
    }

    // 🔹 Regra de negócio: associar ciclo
    public void addCycle(CycleModel cycle) {
        if (cycle == null) {
            throw new IllegalArgumentException("Ciclo não pode ser nulo");
        }
        this.cycles.add(cycle);
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public Set<CycleModel> getCycles() { return cycles; }
    public TeamModel getTeam() { return team; }
}
