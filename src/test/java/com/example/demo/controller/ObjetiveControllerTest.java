package com.example.demo.controller;

import com.example.demo.model.Cycle;
import com.example.demo.model.Objective;
import com.example.demo.model.Team;
import com.example.demo.repository.CycleRepository;
import com.example.demo.repository.ObjectiveRepository;
import com.example.demo.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ObjectiveControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ObjectiveRepository objectiveRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CycleRepository cycleRepository;

    @InjectMocks
    private ObjectiveController objectiveController;

    private Objective objective;
    private Team team;
    private Cycle cycle;

    @BeforeEach
    void setup() {
        // Inicializa os mocks do Mockito
        MockitoAnnotations.openMocks(this);

        // Configura MockMvc standalone com o controller
        mockMvc = MockMvcBuilders.standaloneSetup(objectiveController).build();

        // Configura ObjectMapper para suportar LocalDate / LocalDateTime
        objectMapper.findAndRegisterModules();

        // Criação dos objetos de teste
        team = new Team();
        team.setName("Time A");

        cycle = new Cycle();
        cycle.setName("Q1 2025");
        cycle.setStartDate(LocalDate.of(2025, 1, 1));
        cycle.setEndDate(LocalDate.of(2025, 3, 31));

        objective = new Objective();
        objective.setId(1L);
        objective.setTitle("Objetivo 1");
        objective.setDescription("Descrição do objetivo 1");
        objective.setTeam(team);
        objective.setCycle(cycle);
    }

    // GET /api/objectives
    @Test
    void deveListarTodosObjetivos() throws Exception {
        when(objectiveRepository.findAll()).thenReturn(Arrays.asList(objective));

        mockMvc.perform(get("/api/objectives"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Objetivo 1"));
    }

    // GET /api/objectives/{id} - positivo
    @Test
    void deveBuscarObjectivePorId() throws Exception {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));

        mockMvc.perform(get("/api/objectives/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Objetivo 1"));
    }

    // GET /api/objectives/{id} - negativo
    @Test
    void deveRetornar404QuandoObjectiveNaoExistir() throws Exception {
        when(objectiveRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/objectives/999"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Objetivo não encontrado com o ID: 999"));
    }

    // POST /api/objectives - positivo
    @Test
    void deveCriarObjective() throws Exception {
        when(teamRepository.findByName(any())).thenReturn(Optional.of(team));
        when(cycleRepository.findByName(any())).thenReturn(Optional.of(cycle));
        when(objectiveRepository.save(any())).thenReturn(objective);

        mockMvc.perform(post("/api/objectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(objective)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Objetivo 1"));
    }

    // POST /api/objectives - sem título (negativo)
    @Test
    void deveRetornar400QuandoCriarObjectiveSemTitulo() throws Exception {
        Objective invalidObjective = new Objective();
        invalidObjective.setDescription("Descrição sem título");
        invalidObjective.setTeam(team);
        invalidObjective.setCycle(cycle);

        mockMvc.perform(post("/api/objectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidObjective)))
                .andExpect(status().isBadRequest());
    }

    // POST /api/objectives - sem Team (negativo)
    @Test
    void deveRetornar400QuandoCriarObjectiveSemTeam() throws Exception {
        Objective invalidObjective = new Objective();
        invalidObjective.setTitle("Título sem Team");
        invalidObjective.setDescription("Descrição");
        invalidObjective.setCycle(cycle);

        mockMvc.perform(post("/api/objectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidObjective)))
                .andExpect(status().isBadRequest());
    }

    // POST /api/objectives - sem Cycle (negativo)
    @Test
    void deveRetornar400QuandoCriarObjectiveSemCycle() throws Exception {
        Objective invalidObjective = new Objective();
        invalidObjective.setTitle("Título sem Cycle");
        invalidObjective.setDescription("Descrição");
        invalidObjective.setTeam(team);

        mockMvc.perform(post("/api/objectives")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidObjective)))
                .andExpect(status().isBadRequest());
    }

    // PUT /api/objectives/{id} - positivo
    @Test
    void deveAtualizarObjective() throws Exception {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));
        when(teamRepository.findByName(any())).thenReturn(Optional.of(team));
        when(cycleRepository.findByName(any())).thenReturn(Optional.of(cycle));
        when(objectiveRepository.save(any())).thenReturn(objective);

        objective.setTitle("Objetivo Atualizado");

        mockMvc.perform(put("/api/objectives/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(objective)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Objetivo Atualizado"));
    }

    // PUT /api/objectives/{id} - negativo
    @Test
    void deveRetornar404AoAtualizarObjectiveInexistente() throws Exception {
        when(objectiveRepository.findById(999L)).thenReturn(Optional.empty());

        Objective updateObjective = new Objective();
        updateObjective.setTitle("Atualização inexistente");
        updateObjective.setDescription("Não existe");

        mockMvc.perform(put("/api/objectives/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateObjective)))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Objetivo não encontrado com o ID: 999"));
    }

    // DELETE /api/objectives/{id} - positivo
    @Test
    void deveDeletarObjective() throws Exception {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objective));

        mockMvc.perform(delete("/api/objectives/1"))
                .andExpect(status().isNoContent());
    }

    // DELETE /api/objectives/{id} - negativo
    @Test
    void deveRetornar404AoDeletarObjectiveInexistente() throws Exception {
        when(objectiveRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/objectives/999"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Objetivo não encontrado com o ID: 999"));
    }
}
