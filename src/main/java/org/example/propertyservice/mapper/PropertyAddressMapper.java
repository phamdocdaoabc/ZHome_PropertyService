package org.example.propertyservice.mapper;

import org.example.propertyservice.domain.dto.PropertyAddressDTO;
import org.example.propertyservice.domain.entity.PropertyAddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface PropertyAddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propertyId", ignore = true)
    PropertyAddressEntity toEnity(PropertyAddressDTO dto);

    PropertyAddressDTO toDTO(PropertyAddressEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propertyId", ignore = true)
    void updateEntity(PropertyAddressDTO propertyAddressDTO, @MappingTarget PropertyAddressEntity entity);

}
