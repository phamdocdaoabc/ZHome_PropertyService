package org.example.propertyservice.service;


import org.example.propertyservice.domain.dto.PropertyDTO;
import org.example.propertyservice.domain.dto.PropertyFilterDTO;
import org.example.propertyservice.domain.dto.PropertyPage;
import org.example.propertyservice.domain.enums.PropertyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PropertiesService {
    Long create(PropertyDTO propertyDTO);

    Long update(PropertyDTO propertyDTO);

    PropertyDTO getProperty(Long id);

    Page<PropertyPage> getList(PropertyFilterDTO dto, Pageable pageable);

    void updateStatus(Long propertyId, PropertyStatus status);

    void delete(Long propertyId);

    List<String> getSuggestions(String keyword);
}
