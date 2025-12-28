package org.example.propertyservice.repository;

import org.example.propertyservice.domain.entity.PropertyAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PropertyAddressRepository extends JpaRepository<PropertyAddressEntity, Long>, JpaSpecificationExecutor<PropertyAddressEntity> {
    void deleteAllByPropertyId(Long propertyId);
    PropertyAddressEntity findByPropertyId(Long propertyId);
    List<PropertyAddressEntity> findAllByPropertyIdIn(Collection<Long> propertyIds);
}
