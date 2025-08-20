package org.codeacademy.baltaragisapi.mapper;

import org.codeacademy.baltaragisapi.dto.ArtistDto;
import org.codeacademy.baltaragisapi.entity.ArtistProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArtistMapper {
    ArtistDto toDto(ArtistProfile entity);
}


