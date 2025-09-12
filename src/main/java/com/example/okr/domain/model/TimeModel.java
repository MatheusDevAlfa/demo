package com.example.okr.domain.model;

public class TimeModel {

    private Long id;
    private String name;
    private boolean active;

    public TimeModel(Long id, String name, boolean active) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O time precisa de um nome válido");
        }
        this.id = id;
        this.name = name;
        this.active = active;
    }

//    // 🔹 Regra de negócio: ativar time
//    public void activate() {
//        if (this.active) {
//            throw new IllegalStateException("O time já está ativo");
//        }
//        this.active = true;
//    }
//
//    // 🔹 Regra de negócio: desativar time
//    public void deactivate() {
//        if (!this.active) {
//            throw new IllegalStateException("O time já está inativo");
//        }
//        this.active = false;
//    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}
