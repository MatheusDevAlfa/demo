package com.example.okr.domain.Mapper;

import com.example.okr.domain.model.ObjetivoModel;
import com.example.okr.domain.entity.ObjetivoEntity;

public class ObjetivoMapper {

    public static ObjetivoModel toModel(ObjetivoEntity entity) {
        return new ObjetivoModel(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.isFlagAtivo(),
                TimeMapper.toModel(entity.getTimeEntity()),
                entity.getCicloEntity()
        );
    }

    public static ObjetivoEntity toEntity(ObjetivoModel model) {
        ObjetivoEntity entity = new ObjetivoEntity();
        entity.setId(model.getId());
        entity.setTitulo(model.getTitulo());
        entity.setDescricao(model.getDescrição());
        entity.setFlagAtivo(model.isFlagAtivo());
        entity.setTimeEntity(TimeMapper.toEntity(model.getTeam()));
        return entity;
    }
}
