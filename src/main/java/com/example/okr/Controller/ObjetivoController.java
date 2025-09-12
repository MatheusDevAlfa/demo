package com.example.okr.Controller;

import com.example.okr.DTO.ObjetivoDTO;
import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.service.ObjetivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetivos")
@Tag(name = "Objetivos", description = "CRUD Objetivos")
public class ObjetivoController {

    @Autowired
    private ObjetivoService objetivoService;

    @GetMapping
    @Operation(summary = "Lista todos os objetivos", description = "Retorna todos os objetivos cadastrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum objetivo encontrado")
    })
    public List<ObjetivoEntity> buscarObjetivos() {
        return objetivoService.buscarTodos();
    }

    @GetMapping ("/ativos")
    @Operation(summary = "Lista objetivos ativos", description = "Retorna somente os objetivos ativos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de objetivos ativos retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado objetivo ativo")
    })
    public List<ObjetivoEntity> buscarObjetivosAtivos() {
        return objetivoService.buscarAtivos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca objetivo por ID", description = "Retorna um objetivo específico pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo encontrado"),
            @ApiResponse(responseCode = "404", description = "Objetivo não encontrado")
    })
    public ResponseEntity<ObjetivoEntity> buscarObjetivoPorId(@PathVariable Long id) {
        return objetivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cria um novo objetivo", description = "Cria um objetivo a partir de um DTO")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Objetivo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação")
    })
    public ResponseEntity<ObjetivoEntity> CriarOjetivo(@RequestBody ObjetivoDTO dto) {
        ObjetivoEntity createdObjetivoEntity = objetivoService.criarPorDTO(dto);
        return ResponseEntity.status(201).body(createdObjetivoEntity);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um objetivo", description = "Atualiza os dados de um objetivo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Objetivo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    })
    public ResponseEntity<ObjetivoEntity> atualizarObjetivo(@PathVariable Long id, @RequestBody ObjetivoDTO dto) {
        ObjetivoEntity updatedObjetivoEntity = objetivoService.atualizaPorDTO(id, dto);
        return ResponseEntity.ok(updatedObjetivoEntity);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um objetivo", description = "Realiza exclusão lógica de um objetivo pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Objetivo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Objetivo não encontrado")
    })
    public ResponseEntity<Void> deletarObjetivo (@PathVariable Long id) {
        objetivoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
