//package com.example.okr.domain.Mapper;
//
//import com.example.okr.domain.model.CicloModel;
//import com.example.okr.domain.entity.CicloEntity;
//
//public class CicloMapper {
//
//    public static CicloModel toModel(CicloEntity entity) {
//        return new CicloModel(
//                entity.getId(),
//                entity.getNome(),
//                entity.getInicioData(),
//                entity.getFimData(),
//                entity.isFlagAtivo()
//        );
//    }
//
//    public static CicloEntity toEntity(CicloModel model) {
//        CicloEntity entity = new CicloEntity();
//        entity.setId(model.getId());
//        entity.setNome(model.getName());
//        entity.setInicioData(model.getStartDate());
//        entity.setFimData(model.getFimData());
//        entity.setFlagAtivo(model.isFlagAtivo());
//        return entity;
//    }
//}
