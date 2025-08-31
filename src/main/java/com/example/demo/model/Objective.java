package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "tbl_objeivos") // Nome da tabela no banco de dados
public class Objective {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obj_id") // Nome da coluna de ID
    private Long id;

    @Column(name = "titulo") // Nome da coluna de Título
    private String title;

    @Column(name = "descricao") // Nome da coluna de Descrição
    private String description;

    private boolean completed;

    @Column(name = "data_criacao") // Exemplo: se o nome da coluna fosse diferente
    private LocalDate creationDate;

    @ManyToOne
    @JoinColumn(name = "fk_time") // Nome da coluna de chave estrangeira para Time
    private Team team;

    @ManyToOne
    @JoinColumn(name = "fk_ciclo") // Nome da coluna de chave estrangeira para Ciclo
    private Cycle cycle;
}