package org.example.propertyservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.propertyservice.audit.BaseEntity;
import org.example.propertyservice.domain.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "properties")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PropertyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "transaction_type", length = 50)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "property_type", length = 50)
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(name = "price_min", precision = 18, scale = 2)
    private BigDecimal priceMin;

    @Column(name = "price_max", precision = 18, scale = 2)
    private BigDecimal priceMax;

    @Column(name = "price_negotiable")
    private Boolean priceNegotiable = false;

    @Column(name = "direction", length = 50)
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(name = "area_min")
    private Double areaMin;

    @Column(name = "area_max")
    private Double areaMax;

    @Column(name = "legal_status", length = 50)
    @Enumerated(EnumType.STRING)
    private LegalStatus legalStatus;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "status", length = 50)
    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "rating")
    private Double rating = 0.0;

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "priority")
    private Long priority;

}
