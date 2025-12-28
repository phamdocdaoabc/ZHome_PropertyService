package org.example.propertyservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.propertyservice.audit.BaseEntity;
import org.hibernate.validator.constraints.UniqueElements;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "property_addresses")
@Getter
@Setter
@Builder
public class PropertyAddressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "property_id", nullable = false, unique = true)
    private Long propertyId;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String streetName;
    private String fullAddress;
    private Double latitude;
    private Double longitude;
}
