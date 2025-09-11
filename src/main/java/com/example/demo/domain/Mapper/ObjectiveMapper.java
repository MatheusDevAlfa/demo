package com.example.demo.domain.Mapper;

import com.example.demo.domain.model.ObjectiveModel;
import com.example.demo.domain.entity.ObjectiveEntity;

public class ObjectiveMapper {

    public static ObjectiveModel toModel(ObjectiveEntity entity) {
        return new ObjectiveModel(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.isActive(),
                TeamMapper.toModel(entity.getTeam())
        );
    }

    public static ObjectiveEntity toEntity(ObjectiveModel model) {
        ObjectiveEntity entity = new ObjectiveEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setActive(model.isActive());
        entity.setTeam(TeamMapper.toEntity(model.getTeam()));
        return entity;
    }
}
