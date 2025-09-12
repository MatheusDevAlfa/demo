package com.example.okr.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class ObjetivoDTO {

    private String titulo;
    private String descricao;

    // Nome do time selecionado pelo usuário
    private Long idTime;

    // IDs dos ciclos selecionados pelo usuário
    private Set<Long> idCiclo;
}
