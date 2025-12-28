package org.example.propertyservice.repository.specs;

import jakarta.persistence.criteria.Predicate;
import org.example.propertyservice.domain.entity.PropertyAddressEntity;
import org.springframework.data.jpa.domain.Specification;


public class PropertyAddressSpecification {
    public static Specification<PropertyAddressEntity> hasProvinceName(String provinceName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("provinceName")), "%" + provinceName.toLowerCase() + "%");
    }

    public static Specification<PropertyAddressEntity> hasDistrictName(String districtName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("districtName")), "%" + districtName.toLowerCase() + "%");
    }

    public static Specification<PropertyAddressEntity> hasWardName(String wardName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("wardName")), "%" + wardName.toLowerCase() + "%");
    }

    public static Specification<PropertyAddressEntity> hasStreetName(String streetName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("streetName")), "%" + streetName.toLowerCase() + "%");
    }

    public static Specification<PropertyAddressEntity> withinViewport(
            Double minLat, Double maxLat,
            Double minLng, Double maxLng) {

        return (root, query, criteriaBuilder) -> {
            // 1. Kiểm tra null để tránh lỗi (nếu FE không gửi đủ 4 toạ độ thì bỏ qua filter này)
            if (minLat == null || maxLat == null || minLng == null || maxLng == null) {
                return null;
            }

            // 2. Tạo điều kiện cho Latitude (Vĩ độ): minLat <= lat <= maxLat
            Predicate latPredicate = criteriaBuilder.between(root.get("latitude"), minLat, maxLat);

            // 3. Tạo điều kiện cho Longitude (Kinh độ): minLng <= lng <= maxLng
            Predicate lngPredicate = criteriaBuilder.between(root.get("longitude"), minLng, maxLng);

            // 4. Kết hợp cả 2 bằng AND (Nằm trong khoảng Lat VÀ nằm trong khoảng Lng)
            return criteriaBuilder.and(latPredicate, lngPredicate);
        };
    }
}
