package org.example.propertyservice.repository;

import org.example.propertyservice.domain.entity.PropertyMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyMediaRepository extends JpaRepository<PropertyMediaEntity, Long> {
}
