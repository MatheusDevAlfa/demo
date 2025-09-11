package com.example.okr.repository;

import com.example.okr.domain.entity.CicloEtity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CycleRepository extends JpaRepository<CicloEtity, Long> {
}