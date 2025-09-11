package com.example.okr.service;

import com.example.okr.DTO.ObjetivoDTO;
import com.example.okr.domain.entity.CicloEtity;
import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.domain.entity.TimeEntity;
import com.example.okr.repository.CycleRepository;
import com.example.okr.repository.ObjectiveRepository;
import com.example.okr.repository.TeamRepository;
import com.example.okr.validacoes.ValidarDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ObjectiveService {

    @Autowired
    private  ValidarDTO validarDTO;

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CycleRepository cycleRepository;

    //------------------------ Buscar todos os objetivos ativos ------------------------------
    public List<ObjetivoEntity> findAll() {
        return objectiveRepository.findAll()
                .stream()
                .filter(ObjetivoEntity::isFlagAtivo)
                .toList();
    }

    //------------------------ Buscar objetivo ativo por ID ---------------------------------
    public Optional<ObjetivoEntity> findById(Long id) {
        return objectiveRepository.findById(id)
                .filter(ObjetivoEntity::isFlagAtivo);
    }

    //------------------------ Criar objetivo a partir de DTO --------------------------------
    public ObjetivoEntity createFromDTO(ObjetivoDTO dto) {
        ValidarDTO.validarDTO(dto); // Para validar título, time e ciclos

        TimeEntity timeEntity = teamRepository.findById(dto.getIdTime())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<CicloEtity> cicloEtity = buscarCiclosAtivos(dto.getIdCiclo());

        ObjetivoEntity objetivoEntity = new ObjetivoEntity();
        objetivoEntity.setTitulo(dto.getTitulo());
        objetivoEntity.setDescricao(dto.getDescricao());
        objetivoEntity.setTimeEntity(timeEntity);
        objetivoEntity.setCicloEntity(cicloEtity);
        objetivoEntity.setFlagAtivo(true);

        return objectiveRepository.save(objetivoEntity);
    }

    //------------------------ Atualizar objetivo a partir de DTO ---------------------------
    public ObjetivoEntity updateFromDTO(Long id, ObjetivoDTO dto) {
        ObjetivoEntity existingObjetivoEntity = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));

        if (!existingObjetivoEntity.isFlagAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Objetivo não está ativo e não pode ser alterado");
        }

        ValidarDTO.validarDTO(dto); // Para validar título, time e ciclos

        TimeEntity timeEntity = teamRepository.findById(dto.getIdTime())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time não encontrado"));

        Set<CicloEtity> cicloEtity = buscarCiclosAtivos(dto.getIdCiclo());

        existingObjetivoEntity.setTitulo(dto.getTitulo());
        existingObjetivoEntity.setDescricao(dto.getDescricao());
        existingObjetivoEntity.setTimeEntity(timeEntity);
        existingObjetivoEntity.setCicloEntity(cicloEtity);

        return objectiveRepository.save(existingObjetivoEntity);
    }

    //------------------------ Deletar objetivo (soft delete) --------------------------------
    public ObjetivoEntity delete(Long id) {
        ObjetivoEntity objetivoEntity = objectiveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found with ID " + id));
        objetivoEntity.setFlagAtivo(false);
        return objectiveRepository.save(objetivoEntity);
    }

    //------------------------ Métodos auxiliares -------------------------------------------


    private Set<CicloEtity> buscarCiclosAtivos(Set<Long> cycleIds) {
        Set<CicloEtity> ciclosValidos = new HashSet<>();
        for (Long id : cycleIds) {
            CicloEtity ciclo = cycleRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Cycle não encontrado com ID: " + id));
            if (!ciclo.isActive()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cycle com ID " + id + " não está ativo");
            }
            ciclosValidos.add(ciclo);
        }
        return ciclosValidos;
    }
}
