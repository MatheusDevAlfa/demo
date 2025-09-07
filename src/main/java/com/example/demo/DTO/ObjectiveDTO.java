package com.example.demo.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class ObjectiveDTO {

    private String title;
    private String description;

    // Nome do time selecionado pelo usuário
    private String teamName;

    // IDs dos ciclos selecionados pelo usuário
    private Set<Long> cycleIds;
}
