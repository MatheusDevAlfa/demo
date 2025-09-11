package com.example.demo.repository;

import com.example.demo.domain.entity.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CycleRepository extends JpaRepository<Cycle, Long> {
}