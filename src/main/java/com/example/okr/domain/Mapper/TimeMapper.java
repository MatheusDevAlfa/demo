package com.example.okr.domain.Mapper;

import com.example.okr.domain.entity.TimeEntity;
import com.example.okr.domain.model.TimeModel;

public class TimeMapper {

    public static TimeModel toModel(TimeEntity entity) {
        return new TimeModel(
                entity.getId(),
                entity.getNome(),
                entity.isFlagActivo()
        );
    }

    public static TimeEntity toEntity(TimeModel model) {
        TimeEntity entity = new TimeEntity();
        entity.setId(model.getId());
        entity.setNome(model.getName());
        entity.setFlagActivo(model.isActive());
        return entity;
    }
}
