package org.example.propertyservice.repository.specs;

import jakarta.persistence.criteria.Predicate;
import org.example.propertyservice.domain.entity.PropertyEntity;
import org.example.propertyservice.domain.enums.Direction;
import org.example.propertyservice.domain.enums.LegalStatus;
import org.example.propertyservice.domain.enums.PropertyType;
import org.example.propertyservice.domain.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertySpecification {
    // --- CÁC FILTER CỦA PROPERTY (Giữ nguyên của bạn) ---
    public static Specification<PropertyEntity> hasTransactionType(TransactionType transactionType) {
        return (root, query, cb) -> cb.equal(root.get("transactionType"), transactionType);
    }

    public static Specification<PropertyEntity> hasPropertyType(PropertyType propertyType) {
        return (root, query, cb) -> cb.equal(root.get("propertyType"), propertyType);
    }

    public static Specification<PropertyEntity> hasPriceBetween(BigDecimal requestMin, BigDecimal requestMax) {
        return (root, query, cb) -> {
            // Trường hợp 1: Khách không nhập gì cả -> Lấy hết
            if (requestMin == null && requestMax == null) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            // Logic Overlap:
            // 1. Giá Min của Nhà <= Giá Max khách tìm (Nếu khách có nhập Max)
            if (requestMax != null) {
                // property.priceMin <= requestMax
                predicates.add(cb.lessThanOrEqualTo(root.get("priceMin"), requestMax));
            }

            // 2. Giá Max của Nhà >= Giá Min khách tìm (Nếu khách có nhập Min)
            if (requestMin != null) {
                // property.priceMax >= requestMin
                predicates.add(cb.greaterThanOrEqualTo(root.get("priceMax"), requestMin));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PropertyEntity> hasPriceNegotiable(Boolean priceNegotiable) {
        return (root, query, cb) -> cb.equal(root.get("priceNegotiable"), priceNegotiable);
    }

    public static Specification<PropertyEntity> hasAreaBetween(Double requestMin, Double requestMax) {
        return (root, query, cb) -> {
            // Nếu khách không nhập cả 2 ô diện tích -> Bỏ qua
            if (requestMin == null && requestMax == null) {
                return cb.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            // Logic Giao nhau (Overlap):

            // 1. Khách tìm Max (Ví dụ: tìm nhà dưới 100m2)
            // -> Điều kiện: Diện tích nhỏ nhất của nhà phải <= 100m2
            if (requestMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("areaMin"), requestMax));
            }

            // 2. Khách tìm Min (Ví dụ: tìm nhà trên 50m2)
            // -> Điều kiện: Diện tích lớn nhất của nhà phải >= 50m2
            if (requestMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("areaMax"), requestMin));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<PropertyEntity> hasDirection(Direction direction) {
        return (root, query, cb) -> cb.equal(root.get("direction"), direction);
    }

    public static Specification<PropertyEntity> hasLegalStatus(LegalStatus legalStatus) {
        return (root, query, cb) -> cb.equal(root.get("legalStatus"), legalStatus);
    }

    public static Specification<PropertyEntity> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<PropertyEntity> hasIds(Collection<Long> ids) {
        return (root, query, cb) -> root.get("id").in(ids);
    }

    public static Specification<PropertyEntity> hasSearch(String text) {
        return (root, query, cb) -> {
            String likeText = "%" + text.toLowerCase() + "%";
            Predicate title = cb.like(cb.lower(root.get("title")), likeText);
            Predicate project = cb.like(cb.lower(root.get("projectName")), likeText);
            Predicate desc = cb.like(cb.lower(root.get("description")), likeText);
            return cb.or(title, project, desc);
        };
    }
}
