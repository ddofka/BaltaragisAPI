package org.codeacademy.baltaragisapi.mapper;

import org.codeacademy.baltaragisapi.dto.PageDto;
import org.codeacademy.baltaragisapi.entity.Page;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PageMapper {
    PageDto toDto(Page page);
}


