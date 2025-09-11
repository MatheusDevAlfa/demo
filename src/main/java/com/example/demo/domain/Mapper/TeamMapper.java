package com.example.demo.domain.Mapper;

import com.example.demo.domain.entity.Team;
import com.example.demo.domain.model.TeamModel;

public class TeamMapper {

    public static TeamModel toModel(Team entity) {
        return new TeamModel(
                entity.getId(),
                entity.getName(),
                entity.isActive()
        );
    }

    public static Team toEntity(TeamModel model) {
        Team entity = new Team();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setActive(model.isActive());
        return entity;
    }
}
