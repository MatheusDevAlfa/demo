package com.example.okr.repository;

import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.domain.entity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjetivoRepository extends JpaRepository<ObjetivoEntity, Long> {
            boolean existsByTitulo(String titulo);
            boolean existsByTimeEntityAndFlagAtivo(TimeEntity timeEntity, boolean flagAtivo);
    boolean existsByTimeEntityAndFlagAtivoAndIdNot(TimeEntity timeEntity, boolean flagAtivo, Long id);

    default boolean existeOutroAtivoParaTime(TimeEntity timeEntity, Long id) {
        return existsByTimeEntityAndFlagAtivoAndIdNot(timeEntity, true, id);
    }

}

