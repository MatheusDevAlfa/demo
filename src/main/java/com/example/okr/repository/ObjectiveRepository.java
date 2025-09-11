package com.example.okr.repository;

import com.example.okr.domain.entity.ObjetivoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectiveRepository extends JpaRepository<ObjetivoEntity, Long> {
}