package com.example.okr.domain.model;

import java.time.LocalDate;

public class CicloModel {

    private Long id;
    private String nome;
    private LocalDate inicioData;
    private LocalDate fimData;
    private boolean flagAtivo;

    public CicloModel(Long id, String name, LocalDate startDate, LocalDate fimData, boolean flagAtivo) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O ciclo precisa de um nome válido");
        }
        if (startDate != null && fimData != null && fimData.isBefore(startDate)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à inicial");
        }
        this.id = id;
        this.nome = nome;
        this.inicioData = inicioData;
        this.fimData = fimData;
        this.flagAtivo = flagAtivo;
    }

//    // 🔹 Regra: ativar ciclo
//    public void activate() {
//        if (this.flagAtivo) {
//            throw new IllegalStateException("O ciclo já está ativo");
//        }
//        this.flagAtivo = true;
//    }
//
//    // 🔹 Regra: desativar ciclo
//    public void deactivate() {
//        if (!this.flagAtivo) {
//            throw new IllegalStateException("O ciclo já está inativo");
//        }
//        this.flagAtivo = false;
//    }

    // Getters (sem setters para manter imutabilidade parcial)
    public Long getId() { return id; }
    public String getName() { return nome; }
    public LocalDate getStartDate() { return inicioData; }
    public LocalDate getFimData() { return fimData; }
    public boolean isFlagAtivo() { return flagAtivo; }
}
