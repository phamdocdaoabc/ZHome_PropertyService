package org.example.propertyservice.repository;

import org.example.propertyservice.domain.entity.PropertyMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PropertyMediaRepository extends JpaRepository<PropertyMediaEntity, Long> {
    void deleteAllByPropertyId(Long propertyId);
    List<PropertyMediaEntity> findAllByPropertyId(Long propertyId);

    List<PropertyMediaEntity> findAllByPropertyIdIn(Collection<Long> propertyIds);
}
