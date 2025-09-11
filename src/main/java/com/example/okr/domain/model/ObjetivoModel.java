package com.example.okr.domain.model;

import com.example.okr.domain.entity.CicloEtity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

public class ObjetivoModel {

    private Long id;
    private String titulo;
    private String descrição;
    private boolean flagAtivo;
    private Set<CicloModel> ciclos = new HashSet<>();
    private TimeModel time;

    public ObjetivoModel(Long id, String titulo, String descrição, boolean flagAtivo, TimeModel team, Set<CicloEtity> cicloEtities) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título do objetivo é obrigatório");
        }
        this.id = id;
        this.titulo = titulo;
        this.descrição = descrição;
        this.flagAtivo = flagAtivo;
        this.time = team;
    }

//    // 🔹 Regra de negócio: ativar objetivo
//    public void activate() {
//        if (this.flagAtivo) {
//            throw new IllegalStateException("O objetivo já está ativo");
//        }
//        this.flagAtivo = true;
//    }

//    // 🔹 Regra de negócio: desativar objetivo
//    public void deactivate() {
//        if (!this.flagAtivo) {
//            throw new IllegalStateException("O objetivo já está inativo");
//        }
//        this.flagAtivo = false;
//    }

//    // 🔹 Regra de negócio: associar ciclo
//    public void addCycle(CicloModel cycle) {
//        if (cycle == null) {
//            throw new IllegalArgumentException("Ciclo não pode ser nulo");
//        }
//        this.ciclos.add(cycle);
//    }

    // Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescrição() { return descrição; }
    public boolean isFlagAtivo() { return flagAtivo; }
    public Set<CicloModel> getCycles() { return ciclos; }
    public TimeModel getTeam() { return time; }
}
