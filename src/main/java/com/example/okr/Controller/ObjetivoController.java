package com.example.okr.Controller;

import com.example.okr.DTO.ObjetivoDTO;
import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.service.ObjetivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetivos")
@CrossOrigin(origins = "*")
public class ObjetivoController {

    @Autowired
    private ObjetivoService objetivoService;

    @GetMapping
    public List<ObjetivoEntity> buscarObjetivos() {
        return objetivoService.buscarTodos();
    }

    @GetMapping ("/ativos")
    public List<ObjetivoEntity> buscarObjetivosAtivos() {
        return objetivoService.buscarAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjetivoEntity> buscarObjetivoPorId(@PathVariable Long id) {
        return objetivoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ObjetivoEntity> CriarOjetivo(@RequestBody ObjetivoDTO dto) {
        ObjetivoEntity createdObjetivoEntity = objetivoService.criarPorDTO(dto);
        return ResponseEntity.status(201).body(createdObjetivoEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ObjetivoEntity> atualizarObjetivo(@PathVariable Long id, @RequestBody ObjetivoDTO dto) {
        ObjetivoEntity updatedObjetivoEntity = objetivoService.atualizaPorDTO(id, dto);
        return ResponseEntity.ok(updatedObjetivoEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarObjetivo (@PathVariable Long id) {
        objetivoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
