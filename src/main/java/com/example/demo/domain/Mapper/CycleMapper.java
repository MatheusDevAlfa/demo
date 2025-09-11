package com.example.demo.domain.Mapper;

import com.example.demo.domain.model.CycleModel;
import com.example.demo.domain.entity.Cycle;

public class CycleMapper {

    public static CycleModel toModel(Cycle entity) {
        return new CycleModel(
                entity.getId(),
                entity.getName(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isActive()
        );
    }

    public static Cycle toEntity(CycleModel model) {
        Cycle entity = new Cycle();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setActive(model.isActive());
        return entity;
    }
}
