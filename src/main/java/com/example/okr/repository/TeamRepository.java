package com.example.okr.repository;

import com.example.okr.domain.entity.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<TimeEntity, Long> {
    Optional<TimeEntity> findByName(String name);
}
