package com.example.okr.validacoes;

import com.example.okr.domain.entity.CicloEntity;
import com.example.okr.repository.CicloRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;


@Component
public class BuscarCiclosAtivos {

    private final CicloRepository cicloRepository;

    public BuscarCiclosAtivos(CicloRepository cicloRepository) {
        this.cicloRepository = cicloRepository;
    }

    public Set<CicloEntity> buscarCiclosAtivos(Set<Long> cycleIds) {
        Set<CicloEntity> ciclosValidos = new HashSet<>();
        for (Long id : cycleIds) {
            CicloEntity ciclo = cicloRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Ciclo não encontrado com ID" + id));
            if (!ciclo.isFlagAtivo()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Ciclo com ID " + id + " não está ativo");
            }
            ciclosValidos.add(ciclo);
        }
        return ciclosValidos;
    }
}
