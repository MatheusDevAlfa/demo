package com.example.okr.repository;

import com.example.okr.domain.entity.CicloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CicloRepository extends JpaRepository<CicloEntity, Long> {
}