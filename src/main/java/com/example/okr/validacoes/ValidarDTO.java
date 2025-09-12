package com.example.okr.validacoes;

import com.example.okr.DTO.ObjetivoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ValidarDTO {

    public static void validarDTO(ObjetivoDTO dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Título não pode ser nulo");
        }
        if (dto.getIdTime() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deve ser selecionado pelo menos um time");
        }
        if (dto.getIdCiclo() == null || dto.getIdCiclo().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deve ser selecionado pelo menos um ciclo");
        }
    }
}