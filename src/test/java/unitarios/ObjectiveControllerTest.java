package unitarios;

import com.example.demo.DTO.ObjectiveDTO;
import com.example.demo.controller.ObjectiveController;
import com.example.demo.model.Objective;
import com.example.demo.model.Team;
import com.example.demo.model.Cycle;
import com.example.demo.service.ObjectiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectiveControllerTest {

    @InjectMocks
    private ObjectiveController controller;

    @Mock
    private ObjectiveService objectiveService;

    private Objective objective;
    private ObjectiveDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando objetos de teste
        Team team = new Team();
        team.setName("Time A");

        Cycle cycle = new Cycle();
        cycle.setId(1L);
        cycle.setActive(true);

        objective = new Objective();
        objective.setId(1L);
        objective.setTitle("Objetivo 1");
        objective.setDescription("Descrição 1");
        objective.setTeam(team);
        objective.setCycles(Set.of(cycle));
        objective.setActive(true);

        dto = new ObjectiveDTO();
        dto.setTitle("Novo Objetivo");
        dto.setDescription("Descrição Novo Objetivo");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(1L));
    }

    //--------------------- TESTE getAllObjectives -----------------------------------
    @Test
    void testGetAllObjectives_ReturnsList() {
        when(objectiveService.findAll()).thenReturn(List.of(objective));

        List<Objective> result = controller.getAllObjectives();

        // Verificação: lista retornada não é nula e contém o objetivo esperado
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Objetivo 1", result.get(0).getTitle());
    }

    //--------------------- TESTE getObjectiveById -----------------------------------
    @Test
    void testGetObjectiveById_Found() {
        when(objectiveService.findById(1L)).thenReturn(Optional.of(objective));

        ResponseEntity<Objective> response = controller.getObjectiveById(1L);

        // Verificação: status 200 OK e body correto
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Objetivo 1", response.getBody().getTitle());
    }

    @Test
    void testGetObjectiveById_NotFound() {
        when(objectiveService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Objective> response = controller.getObjectiveById(99L);

        // Verificação: status 404 Not Found e body nulo
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    //--------------------- TESTE createObjective -----------------------------------
    @Test
    void testCreateObjective_Success() {
        when(objectiveService.createFromDTO(dto)).thenReturn(objective);

        ResponseEntity<Objective> response = controller.createObjective(dto);

        // Verificação: status 201 Created e body correto
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Objetivo 1", response.getBody().getTitle());
    }

    //--------------------- TESTE updateObjective -----------------------------------
    @Test
    void testUpdateObjective_Success() {
        when(objectiveService.updateFromDTO(1L, dto)).thenReturn(objective);

        ResponseEntity<Objective> response = controller.updateObjective(1L, dto);

        // Verificação: status 200 OK e body correto
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Objetivo 1", response.getBody().getTitle());
    }

    @Test
    void testUpdateObjective_ThrowsException() {
        // Simula exceção lançada pelo service
        when(objectiveService.updateFromDTO(1L, dto))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Erro"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> controller.updateObjective(1L, dto));

        // Verifica mensagem da exceção
        assertEquals("Erro", ex.getReason());
    }

    //--------------------- TESTE deleteObjective -----------------------------------
    @Test
    void testDeleteObjective_Success() {
        // Mock do service para retornar o objetivo inativo após deleção
        when(objectiveService.delete(1L)).thenReturn(objective);

        ResponseEntity<Void> response = controller.deleteObjective(1L);

        // Verificação: status 204 No Content
        assertEquals(204, response.getStatusCodeValue());

        // Verifica se o service foi chamado exatamente uma vez
        verify(objectiveService, times(1)).delete(1L);
    }


    @Test
    void testDeleteObjective_ThrowsException() {
        // Simula exceção lançada pelo service
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Não encontrado"))
                .when(objectiveService).delete(99L);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> controller.deleteObjective(99L));

        // Verifica mensagem da exceção
        assertEquals("Não encontrado", ex.getReason());
    }
}
