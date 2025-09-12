package com.example.okr.repository;

import com.example.okr.domain.entity.ObjetivoEntity;
import com.example.okr.domain.entity.TimeEntity;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.Optional;

public interface ObjetivoRepository extends JpaRepository<ObjetivoEntity, Long> {
            boolean existsByTitulo(String titulo);
            boolean existsByTimeEntityAndFlagAtivoAndIdNot(TimeEntity timeEntity, boolean flagAtivo, Long id);

    default boolean existeOutroAtivoParaTime(TimeEntity timeEntity, Long id) {
        return existsByTimeEntityAndFlagAtivoAndIdNot(timeEntity, true, id);
    }
}

