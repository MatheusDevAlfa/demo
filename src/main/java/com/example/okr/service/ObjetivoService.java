package com.example.okr.service;

import com.example.okr.DTO.ObjetivoDTO;
import com.example.okr.domain.entity.CicloEntity;
import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.domain.entity.TimeEntity;
import com.example.okr.repository.CicloRepository;
import com.example.okr.repository.ObjetivoRepository;
import com.example.okr.repository.TimeRepository;
import com.example.okr.validacoes.BuscarCiclosAtivos;
import com.example.okr.validacoes.ValidarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class ObjetivoService {


    @Autowired
    private BuscarCiclosAtivos buscarCiclosAtivos;

    @Autowired
    private ValidarDTO validarDTO;

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private CicloRepository cicloRepository;

    //------------------------ Buscar todos os objetivos ------------------------------
    public List<ObjetivoEntity> buscarTodos() {
        return objetivoRepository.findAll();
    }

    //------------------------ Buscar objetivos ativos ------------------------------
    public List<ObjetivoEntity> buscarAtivos() {
        return objetivoRepository.findAll()
                .stream()
                .filter(ObjetivoEntity::isFlagAtivo)
                .toList();
    }

    //------------------------ Buscar objetivo ativo por ID ---------------------------------
    public Optional<ObjetivoEntity> buscarPorId(Long id) {
        return objetivoRepository.findById(id)
                .filter(ObjetivoEntity::isFlagAtivo);
    }

    //------------------------ Criar objetivo a partir de DTO --------------------------------
    public ObjetivoEntity criarPorDTO(ObjetivoDTO dto) {
        ValidarDTO.validarDTO(dto); // Para validar título, time e ciclos
        if (objetivoRepository.existsByTitulo(dto.getTitulo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Já existe um objetivo com esse título"
            );
        }
        TimeEntity timeEntity = timeRepository.findById(dto.getIdTime())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Time com ID" + id + "não encontrado"));
        if (!timeEntity.isFlagActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O time selecionado está inativo");
        }
        if (objetivoRepository.existeOutroAtivoParaTime(timeEntity, null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Esse time já possui outro objetivo ativo");
        }

        Set<CicloEntity> cicloEntity = buscarCiclosAtivos.buscarCiclosAtivos(dto.getIdCiclo());


        ObjetivoEntity objetivoEntity = new ObjetivoEntity();
        objetivoEntity.setTitulo(dto.getTitulo());
        objetivoEntity.setDescricao(dto.getDescricao());
        objetivoEntity.setTimeEntity(timeEntity);
        objetivoEntity.setCicloEntity(cicloEntity);
        objetivoEntity.setFlagAtivo(true);

        return objetivoRepository.save(objetivoEntity);
    }

    //------------------------ Atualizar objetivo a partir de DTO ---------------------------
    public ObjetivoEntity atualizaPorDTO(Long id, ObjetivoDTO dto) {
        ObjetivoEntity existingObjetivoEntity = objetivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Objetivo não encontrado com ID " + id));

        if (!existingObjetivoEntity.isFlagAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O objetivo não está ativo");
        }

        ValidarDTO.validarDTO(dto); // Para validar título, time e ciclos

        TimeEntity timeEntity = timeRepository.findById(dto.getIdTime())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Time não encontrado"));

        if (!timeEntity.isFlagActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O time selecionado está inativo");
        }

//        if (objetivoRepository.existsByTimeEntityAndFlagAtivo(timeEntity, true)) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Esse time já possui um objetivo ativo");
        if (objetivoRepository.existeOutroAtivoParaTime(timeEntity, existingObjetivoEntity.getId()))  {
            throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Esse time já possui outro objetivo ativo");
            }

        Set<CicloEntity> cicloEntity = buscarCiclosAtivos.buscarCiclosAtivos(dto.getIdCiclo());

        boolean existeCicloInativo = cicloEntity.stream()
                .anyMatch(c -> !c.isFlagAtivo());
        if (existeCicloInativo) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O ciclo selecionado está inativo");
            }

        existingObjetivoEntity.setTitulo(dto.getTitulo());
        existingObjetivoEntity.setDescricao(dto.getDescricao());
        existingObjetivoEntity.setTimeEntity(timeEntity);
        existingObjetivoEntity.setCicloEntity(cicloEntity);

        return objetivoRepository.save(existingObjetivoEntity);
    }

    //------------------------ Deletar objetivo (soft delete) --------------------------------
    public ObjetivoEntity deletar(Long id) {
        ObjetivoEntity objetivoEntity = objetivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Objetivo não encontrado com ID " + id));
        if (!objetivoEntity.isFlagAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Objetivo com ID " + id + " já está deletado");
        }
        objetivoEntity.setFlagAtivo(false);
        return objetivoRepository.save(objetivoEntity);
    }
}