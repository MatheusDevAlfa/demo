//package unitarios;
//
//import com.example.okr.DTO.ObjetivoDTO;
//import com.example.okr.domain.entity.CicloEtity;
//import com.example.okr.domain.entity.ObjetivoEntity;
//import com.example.okr.domain.entity.TimeEntity;
//import com.example.okr.repository.CycleRepository;
//import com.example.okr.repository.ObjectiveRepository;
//import com.example.okr.repository.TeamRepository;
//import com.example.okr.service.ObjectiveService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ObjectiveModelEntityServiceTest {
//
//    @InjectMocks
//    private ObjectiveService objectiveService;
//
//    @Mock
//    private ObjectiveRepository objectiveRepository;
//
//    @Mock
//    private TeamRepository teamRepository;
//
//    @Mock
//    private CycleRepository cycleRepository;
//
//    private TimeEntity timeEntity;
//    private CicloEtity activeCicloEtity;
//    private CicloEtity inactiveCicloEtity;
//    private ObjetivoEntity objetivoEntity;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Criando um time de teste
//        timeEntity = new TimeEntity();
//        timeEntity.setName("Time A");
//
//        // Ciclos para teste
//        activeCicloEtity = new CicloEtity();
//        activeCicloEtity.setId(1L);
//        activeCicloEtity.setActive(true);
//
//        inactiveCicloEtity = new CicloEtity();
//        inactiveCicloEtity.setId(2L);
//        inactiveCicloEtity.setActive(false);
//
//        // Objetivo de teste
//        objetivoEntity = new ObjetivoEntity();
//        objetivoEntity.setId(1L);
//        objetivoEntity.setTitle("Objetivo 1");
//        objetivoEntity.setDescription("Descrição 1");
//        objetivoEntity.setTimeEntity(timeEntity);
//        objetivoEntity.setCicloEtities(Set.of(activeCicloEtity));
//        objetivoEntity.setActive(true);
//    }
//
//    //--------------------- TESTE findAll -----------------------------------
//    @Test
//    void testFindAll_ReturnsOnlyActiveObjectives() {
//        ObjetivoEntity inactiveObjetivoEntity = new ObjetivoEntity();
//        inactiveObjetivoEntity.setActive(false);
//
//        // Retorna lista com objetivo ativo e inativo
//        when(objectiveRepository.findAll()).thenReturn(List.of(objetivoEntity, inactiveObjetivoEntity));
//
//        List<ObjetivoEntity> result = objectiveService.findAll();
//
//        // Apenas objetivos ativos devem ser retornados
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).isActive());
//    }
//
//    //--------------------- TESTE findById -----------------------------------
//    @Test
//    void testFindById_ReturnsActiveObjective() {
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//
//        Optional<ObjetivoEntity> result = objectiveService.findById(1L);
//
//        // Objetivo ativo deve ser retornado
//        assertTrue(result.isPresent());
//        assertEquals("Objetivo 1", result.get().getTitle());
//    }
//
//    @Test
//    void testFindById_InactiveObjective_ReturnsEmpty() {
//        objetivoEntity.setActive(false);
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//
//        Optional<ObjetivoEntity> result = objectiveService.findById(1L);
//
//        // Objetivo inativo deve retornar Optional vazio
//        assertTrue(result.isEmpty());
//    }
//
//    //--------------------- TESTES createFromDTO -----------------------------------
//    @Test
//    void testCreateFromDTO_Success() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Novo Objetivo");
//        dto.setDescription("Descrição Novo Objetivo");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(1L));
//
//        // Mock dos repositórios
//        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(timeEntity));
//        when(cycleRepository.findById(1L)).thenReturn(Optional.of(activeCicloEtity));
//        when(objectiveRepository.save(any(ObjetivoEntity.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        ObjetivoEntity result = objectiveService.createFromDTO(dto);
//
//        // Verifica se os campos foram atribuídos corretamente
//        assertEquals("Novo Objetivo", result.getTitle());
//        assertEquals(timeEntity, result.getTimeEntity());
//        assertTrue(result.isActive());
//    }
//
//    @Test
//    void testCreateFromDTO_ThrowsException_WhenTitleNullOrBlank() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("  ");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(1L));
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.createFromDTO(dto));
//        assertEquals("Título não pode ser nulo ou vazio", ex.getReason());
//    }
//
//    @Test
//    void testCreateFromDTO_ThrowsException_WhenTeamNotProvided() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Título");
//        dto.setTeamName("  ");
//        dto.setCycleIds(Set.of(1L));
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.createFromDTO(dto));
//        assertEquals("Team deve ser informado", ex.getReason());
//    }
//
//    @Test
//    void testCreateFromDTO_ThrowsException_WhenCycleIdsEmpty() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Título");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Collections.emptySet());
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.createFromDTO(dto));
//        assertEquals("Deve ser selecionado pelo menos um ciclo", ex.getReason());
//    }
//
//    @Test
//    void testCreateFromDTO_ThrowsException_WhenTeamNotFound() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Título");
//        dto.setTeamName("Time Desconhecido");
//        dto.setCycleIds(Set.of(1L));
//
//        when(teamRepository.findByName("Time Desconhecido")).thenReturn(Optional.empty());
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.createFromDTO(dto));
//        assertEquals("Time não encontrado", ex.getReason());
//    }
//
//    @Test
//    void testCreateFromDTO_ThrowsException_WhenCycleNotFound() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Título");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(99L));
//
//        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(timeEntity));
//        when(cycleRepository.findById(99L)).thenReturn(Optional.empty());
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.createFromDTO(dto));
//        assertEquals("Cycle não encontrado com ID: 99", ex.getReason());
//    }
//
//    @Test
//    void testCreateFromDTO_ThrowsException_WhenCycleInactive() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Título");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(2L));
//
//        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(timeEntity));
//        when(cycleRepository.findById(2L)).thenReturn(Optional.of(inactiveCicloEtity));
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.createFromDTO(dto));
//        assertEquals("Cycle com ID 2 não está ativo", ex.getReason());
//    }
//
//    //--------------------- TESTES updateFromDTO -----------------------------------
//    @Test
//    void testUpdateFromDTO_Success() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Atualizado");
//        dto.setDescription("Atualizado Desc");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(1L));
//
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(timeEntity));
//        when(cycleRepository.findById(1L)).thenReturn(Optional.of(activeCicloEtity));
//        when(objectiveRepository.save(any(ObjetivoEntity.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        ObjetivoEntity result = objectiveService.updateFromDTO(1L, dto);
//
//        assertEquals("Atualizado", result.getTitle());
//        assertEquals("Atualizado Desc", result.getDescription());
//        assertEquals(timeEntity, result.getTimeEntity());
//    }
//
//    @Test
//    void testUpdateFromDTO_ThrowsException_WhenObjectiveInactive() {
//        objetivoEntity.setActive(false);
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Teste");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(1L));
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.updateFromDTO(1L, dto));
//        assertEquals("Objetivo não está ativo e não pode ser alterado", ex.getReason());
//    }
//
//    @Test
//    void testUpdateFromDTO_ThrowsException_WhenObjectiveNotFound() {
//        when(objectiveRepository.findById(99L)).thenReturn(Optional.empty());
//
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Teste");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(1L));
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.updateFromDTO(99L, dto));
//        assertEquals("Objective not found with ID 99", ex.getReason());
//    }
//
//    @Test
//    void testUpdateFromDTO_ThrowsException_WhenTeamNotFound() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Teste");
//        dto.setTeamName("Time Desconhecido");
//        dto.setCycleIds(Set.of(1L));
//
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//        when(teamRepository.findByName("Time Desconhecido")).thenReturn(Optional.empty());
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.updateFromDTO(1L, dto));
//        assertEquals("Time não encontrado", ex.getReason());
//    }
//
//    @Test
//    void testUpdateFromDTO_ThrowsException_WhenCycleInactive() {
//        ObjetivoDTO dto = new ObjetivoDTO();
//        dto.setTitle("Teste");
//        dto.setTeamName("Time A");
//        dto.setCycleIds(Set.of(2L));
//
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//        when(teamRepository.findByName("Time A")).thenReturn(Optional.of(timeEntity));
//        when(cycleRepository.findById(2L)).thenReturn(Optional.of(inactiveCicloEtity));
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.updateFromDTO(1L, dto));
//        assertEquals("Cycle com ID 2 não está ativo", ex.getReason());
//    }
//
//    //--------------------- TESTES delete -----------------------------------
//    @Test
//    void testDelete_Success() {
//        when(objectiveRepository.findById(1L)).thenReturn(Optional.of(objetivoEntity));
//        when(objectiveRepository.save(any(ObjetivoEntity.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        ObjetivoEntity result = objectiveService.delete(1L);
//
//        // Objetivo deve ser marcado como inativo
//        assertFalse(result.isActive());
//    }
//
//    @Test
//    void testDelete_ThrowsException_WhenObjectiveNotFound() {
//        when(objectiveRepository.findById(99L)).thenReturn(Optional.empty());
//
//        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
//                () -> objectiveService.delete(99L));
//        assertEquals("Objective not found with ID 99", ex.getReason());
//    }
//}
