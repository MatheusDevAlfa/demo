package com.example.demo.repository;

import com.example.demo.domain.entity.ObjectiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectiveRepository extends JpaRepository<ObjectiveEntity, Long> {
}