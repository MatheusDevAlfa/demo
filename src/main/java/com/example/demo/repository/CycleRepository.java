package com.example.demo.repository;

import com.example.demo.model.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {
    Optional<Cycle> findByName(String name);
}