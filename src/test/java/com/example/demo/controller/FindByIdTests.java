package com.example.demo.controller;

import com.example.demo.model.Cycle;
import com.example.demo.model.Objective;
import com.example.demo.model.Team;
import com.example.demo.repository.CycleRepository;
import com.example.demo.repository.ObjectiveRepository;
import com.example.demo.repository.TeamRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FindByIdTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectiveRepository objectiveRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private CycleRepository cycleRepository;

    private Team team;
    private Cycle cycle;

    @BeforeEach
    public void setup() {
        // Prepara dados que serão usados em todos os testes
        team = new Team();
        team.setName("Vendas");
        teamRepository.save(team);

        cycle = new Cycle();
        cycle.setName("Q1 2024");
        cycleRepository.save(cycle);
    }

    @AfterEach
    public void cleanup() {
        // Limpa todas as tabelas após cada teste
        objectiveRepository.deleteAll();
        teamRepository.deleteAll();
        cycleRepository.deleteAll();
    }

    @Test
    void testFindObjectiveById() {
        // 1. Configurar: Cria e salva um objetivo no banco de dados
        Objective objective = new Objective();
        objective.setTitle("Lançar novo produto");
        objective.setDescription("Planejar e executar o lançamento do produto X.");
        objective.setTeam(team);
        objective.setCycle(cycle);
        objectiveRepository.save(objective);

        // 2. Executar: Faz a requisição GET para a API
        String url = "http://localhost:" + port + "/api/objectives/" + objective.getId();
        ResponseEntity<Objective> response = restTemplate.getForEntity(url, Objective.class);

        // 3. Afirmar: Verifica a resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(objective.getId(), response.getBody().getId());
        assertEquals("Lançar novo produto", response.getBody().getTitle());
    }

    @Test
    void testFindObjectiveByIdNotFound() {
        // Executar: Faz a requisição GET para um ID que não existe
        String url = "http://localhost:" + port + "/api/objectives/999";
        ResponseEntity<Objective> response = restTemplate.getForEntity(url, Objective.class);

        // Afirmar: Verifica se a API retorna o status NOT_FOUND
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}