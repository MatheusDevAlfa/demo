package com.example.okr.domain.Mapper;

import com.example.okr.domain.model.CicloModel;
import com.example.okr.domain.entity.CicloEtity;

public class CicloMapper {

    public static CicloModel toModel(CicloEtity entity) {
        return new CicloModel(
                entity.getId(),
                entity.getNome(),
                entity.getInicioData(),
                entity.getFimData(),
                entity.isActive()
        );
    }

    public static CicloEtity toEntity(CicloModel model) {
        CicloEtity entity = new CicloEtity();
        entity.setId(model.getId());
        entity.setNome(model.getName());
        entity.setInicioData(model.getStartDate());
        entity.setFimData(model.getFimData());
        entity.setActive(model.isFlagAtivo());
        return entity;
    }
}
