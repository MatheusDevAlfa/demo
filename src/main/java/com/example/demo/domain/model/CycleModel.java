package com.example.demo.domain.model;

import java.time.LocalDate;

public class CycleModel {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public CycleModel(Long id, String name, LocalDate startDate, LocalDate endDate, boolean active) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O ciclo precisa de um nome válido");
        }
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à inicial");
        }
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    // 🔹 Regra: ativar ciclo
    public void activate() {
        if (this.active) {
            throw new IllegalStateException("O ciclo já está ativo");
        }
        this.active = true;
    }

    // 🔹 Regra: desativar ciclo
    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("O ciclo já está inativo");
        }
        this.active = false;
    }

    // Getters (sem setters para manter imutabilidade parcial)
    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }
}
