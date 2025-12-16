package org.example.propertyservice.repository;

import org.example.propertyservice.domain.entity.PropertyAddressesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyAddressesRepository extends JpaRepository<PropertyAddressesEntity, Long> {
}
