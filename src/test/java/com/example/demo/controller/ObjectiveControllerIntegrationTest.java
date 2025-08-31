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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Usa um perfil de teste
public class ObjectiveControllerIntegrationTest {

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
    void testCreateObjective() {
        // Cria um objetivo de teste
        Objective newObjective = new Objective();
        newObjective.setTitle("Aumentar vendas em 10%");
        newObjective.setDescription("Focar em novos clientes");
        newObjective.setTeam(team);
        newObjective.setCycle(cycle);

        // Envia a requisição POST para a API
        ResponseEntity<Objective> response = restTemplate.postForEntity("/api/objectives", newObjective, Objective.class);

        // Verifica a resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Aumentar vendas em 10%", response.getBody().getTitle());
    }

    @Test
    void testFindAllObjectives() {
        // Cria um objetivo diretamente no banco para o teste de leitura
        Objective objective = new Objective();
        objective.setTitle("Lançar novo produto");
        objective.setTeam(team);
        objective.setCycle(cycle);
        objectiveRepository.save(objective);

        // Envia a requisição GET para a API
        ResponseEntity<Objective[]> response = restTemplate.getForEntity("/api/objectives", Objective[].class);

        // Verifica a resposta
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
        assertEquals("Lançar novo produto", response.getBody()[0].getTitle());
    }

    @Test
    void testUpdateObjective() {
        // Cria um objetivo que será atualizado
        Objective objective = new Objective();
        objective.setTitle("Melhorar satisfação do cliente");
        objective.setTeam(team);
        objective.setCycle(cycle);
        objectiveRepository.save(objective);

        // Modifica o objetivo para a atualização
        objective.setTitle("Melhorar satisfação do cliente em 20%");

        // Envia a requisição PUT para a API
        String url = "/api/objectives/" + objective.getId();
        ResponseEntity<Objective> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(objective), Objective.class);

        // Verifica se a requisição foi bem-sucedida
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verifica se a atualização foi feita no banco de dados
        Objective updatedObjective = objectiveRepository.findById(objective.getId()).orElse(null);
        assertNotNull(updatedObjective);
        assertEquals("Melhorar satisfação do cliente em 20%", updatedObjective.getTitle());
    }

    @Test
    void testDeleteObjective() {
        // Cria um objetivo que será excluído
        Objective objective = new Objective();
        objective.setTitle("Otimizar processos internos");
        objective.setTeam(team);
        objective.setCycle(cycle);
        objectiveRepository.save(objective);

        // Envia a requisição DELETE para a API
        String url = "/api/objectives/" + objective.getId();
        restTemplate.delete(url);

        // Verifica se o objetivo não existe mais no banco de dados
        assertFalse(objectiveRepository.findById(objective.getId()).isPresent());
    }
}