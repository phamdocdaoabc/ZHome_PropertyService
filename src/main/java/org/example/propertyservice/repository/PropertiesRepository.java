package org.example.propertyservice.repository;

import org.example.propertyservice.domain.entity.PropertiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PropertiesRepository extends JpaRepository<PropertiesEntity, Long> {

}
