package org.example.propertyservice.mapper;

import org.example.propertyservice.domain.dto.PropertyDTO;
import org.example.propertyservice.domain.dto.PropertyPage;
import org.example.propertyservice.domain.entity.PropertyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PropertiesMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    PropertyEntity toEnity(PropertyDTO propertyDTO);

    PropertyDTO toDTO(PropertyEntity propertyEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateEntity(PropertyDTO propertyDTO, @MappingTarget PropertyEntity propertyEntity);

    PropertyPage toPropertyPage(PropertyEntity propertyEntity);
}
