package com.github.losevskiyfz.mapper;

import com.github.losevskiyfz.dto.PlayerDto;
import com.github.losevskiyfz.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    Player toEntity(PlayerDto playerDto);
    PlayerDto toDto(Player player);
}
