package unitarios;

import com.example.demo.DTO.ObjectiveDTO;
import com.example.demo.domain.entity.Cycle;
import com.example.demo.domain.entity.ObjectiveEntity;
import com.example.demo.domain.entity.Team;
import com.example.demo.repository.CycleRepository;
import com.example.demo.repository.ObjectiveRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.ObjectiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectiveModelEntityServiceTest {

    @InjectMocks
    private ObjectiveService objectiveService;

    @Mock
    private ObjectiveRepository objectiveRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CycleRepository cycleRepository;

    private Team team;
    private Cycle activeCycle;
    private Cycle inactiveCycle;
    private ObjectiveEntity objectiveEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando um time de teste
        team = new Team();
        team.setName("Time A");

        // Ciclos para teste
        activeCycle = new Cycle();
        activeCycle.setId(1L);
        activeCycle.setActive(true);

        inactiveCycle = new Cycle();
        inactiveCycle.setId(2L);
        inactiveCycle.setActive(false);

        // Objetivo de teste
        objectiveEntity = new ObjectiveEntity();
        objectiveEntity.setId(1L);
        objectiveEntity.setTitle("Objetivo 1");
        objectiveEntity.setDescription("Descrição 1");
        objectiveEntity.setTeam(team);
        objectiveEntity.setCycles(Set.of(activeCycle));
        objectiveEntity.setActive(true);
    }

    //--------------------- TESTE findAll -----------------------------------
    @Test
    void testFindAll_ReturnsOnlyActiveObjectives() {
        ObjectiveEntity inactiveObjectiveEntity = new ObjectiveEntity();
        inactiveObjectiveEntity.setActive(false);

        // Retorna lista com objetivo ativo e inativo
        when(objectiveRepository.findAll()).thenReturn(List.of(objectiveEntity, inactiveObjectiveEntity));

        List<ObjectiveEntity> result = objectiveService.findAll();

        // Apenas objetivos ativos devem ser retornados
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
    }

    //--------------------- TESTE findById -----------------------------------
    @Test
    void testFindById_ReturnsActiveObjective() {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));

        Optional<ObjectiveEntity> result = objectiveService.findById(1L);

        // Objetivo ativo deve ser retornado
        assertTrue(result.isPresent());
        assertEquals("Objetivo 1", result.get().getTitle());
    }

    @Test
    void testFindById_InactiveObjective_ReturnsEmpty() {
        objectiveEntity.setActive(false);
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));

        Optional<ObjectiveEntity> result = objectiveService.findById(1L);

        // Objetivo inativo deve retornar Optional vazio
        assertTrue(result.isEmpty());
    }

    //--------------------- TESTES createFromDTO -----------------------------------
    @Test
    void testCreateFromDTO_Success() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Novo Objetivo");
        dto.setDescription("Descrição Novo Objetivo");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(1L));

        // Mock dos repositórios
        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(team));
        when(cycleRepository.findById(1L)).thenReturn(Optional.of(activeCycle));
        when(objectiveRepository.save(any(ObjectiveEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ObjectiveEntity result = objectiveService.createFromDTO(dto);

        // Verifica se os campos foram atribuídos corretamente
        assertEquals("Novo Objetivo", result.getTitle());
        assertEquals(team, result.getTeam());
        assertTrue(result.isActive());
    }

    @Test
    void testCreateFromDTO_ThrowsException_WhenTitleNullOrBlank() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("  ");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(1L));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.createFromDTO(dto));
        assertEquals("Título não pode ser nulo ou vazio", ex.getReason());
    }

    @Test
    void testCreateFromDTO_ThrowsException_WhenTeamNotProvided() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Título");
        dto.setTeamName("  ");
        dto.setCycleIds(Set.of(1L));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.createFromDTO(dto));
        assertEquals("Team deve ser informado", ex.getReason());
    }

    @Test
    void testCreateFromDTO_ThrowsException_WhenCycleIdsEmpty() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Título");
        dto.setTeamName("Time A");
        dto.setCycleIds(Collections.emptySet());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.createFromDTO(dto));
        assertEquals("Deve ser selecionado pelo menos um ciclo", ex.getReason());
    }

    @Test
    void testCreateFromDTO_ThrowsException_WhenTeamNotFound() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Título");
        dto.setTeamName("Time Desconhecido");
        dto.setCycleIds(Set.of(1L));

        when(teamRepository.findByName("Time Desconhecido")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.createFromDTO(dto));
        assertEquals("Time não encontrado", ex.getReason());
    }

    @Test
    void testCreateFromDTO_ThrowsException_WhenCycleNotFound() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Título");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(99L));

        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(team));
        when(cycleRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.createFromDTO(dto));
        assertEquals("Cycle não encontrado com ID: 99", ex.getReason());
    }

    @Test
    void testCreateFromDTO_ThrowsException_WhenCycleInactive() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Título");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(2L));

        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(team));
        when(cycleRepository.findById(2L)).thenReturn(Optional.of(inactiveCycle));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.createFromDTO(dto));
        assertEquals("Cycle com ID 2 não está ativo", ex.getReason());
    }

    //--------------------- TESTES updateFromDTO -----------------------------------
    @Test
    void testUpdateFromDTO_Success() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Atualizado");
        dto.setDescription("Atualizado Desc");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(1L));

        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));
        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(team));
        when(cycleRepository.findById(1L)).thenReturn(Optional.of(activeCycle));
        when(objectiveRepository.save(any(ObjectiveEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ObjectiveEntity result = objectiveService.updateFromDTO(1L, dto);

        assertEquals("Atualizado", result.getTitle());
        assertEquals("Atualizado Desc", result.getDescription());
        assertEquals(team, result.getTeam());
    }

    @Test
    void testUpdateFromDTO_ThrowsException_WhenObjectiveInactive() {
        objectiveEntity.setActive(false);
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));

        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Teste");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(1L));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.updateFromDTO(1L, dto));
        assertEquals("Objetivo não está ativo e não pode ser alterado", ex.getReason());
    }

    @Test
    void testUpdateFromDTO_ThrowsException_WhenObjectiveNotFound() {
        when(objectiveRepository.findById(99L)).thenReturn(Optional.empty());

        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Teste");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(1L));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.updateFromDTO(99L, dto));
        assertEquals("Objective not found with ID 99", ex.getReason());
    }

    @Test
    void testUpdateFromDTO_ThrowsException_WhenTeamNotFound() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Teste");
        dto.setTeamName("Time Desconhecido");
        dto.setCycleIds(Set.of(1L));

        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));
        when(teamRepository.findByName("Time Desconhecido")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.updateFromDTO(1L, dto));
        assertEquals("Time não encontrado", ex.getReason());
    }

    @Test
    void testUpdateFromDTO_ThrowsException_WhenCycleInactive() {
        ObjectiveDTO dto = new ObjectiveDTO();
        dto.setTitle("Teste");
        dto.setTeamName("Time A");
        dto.setCycleIds(Set.of(2L));

        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));
        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(team));
        when(cycleRepository.findById(2L)).thenReturn(Optional.of(inactiveCycle));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.updateFromDTO(1L, dto));
        assertEquals("Cycle com ID 2 não está ativo", ex.getReason());
    }

    //--------------------- TESTES delete -----------------------------------
    @Test
    void testDelete_Success() {
        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objectiveEntity));
        when(objectiveRepository.save(any(ObjectiveEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ObjectiveEntity result = objectiveService.delete(1L);

        // Objetivo deve ser marcado como inativo
        assertFalse(result.isActive());
    }

    @Test
    void testDelete_ThrowsException_WhenObjectiveNotFound() {
        when(objectiveRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> objectiveService.delete(99L));
        assertEquals("Objective not found with ID 99", ex.getReason());
    }
}
