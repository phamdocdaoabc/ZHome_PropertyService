package org.example.propertyservice.repository;

import org.example.propertyservice.domain.entity.PropertyEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PropertyRepository extends JpaRepository<PropertyEntity, Long>, JpaSpecificationExecutor<PropertyEntity> {
    @Query("SELECT p.projectName FROM PropertyEntity p " +
            "WHERE lower(p.projectName) LIKE lower(concat('%', :keyword, '%')) " +
            "AND p.projectName IS NOT NULL " +
            "GROUP BY p.projectName " +
            "ORDER BY MIN(p.priority) ASC")
    List<String> searchProjectNames(@Param("keyword") String keyword, Pageable pageable);

    // 2. Tìm theo Tiêu đề (Sắp xếp theo priority tăng dần)
    @Query("SELECT p.title FROM PropertyEntity p " +
            "WHERE lower(p.title) LIKE lower(concat('%', :keyword, '%')) " +
            "ORDER BY p.priority ASC")
    List<String> searchTitles(@Param("keyword") String keyword, Pageable pageable);
}
