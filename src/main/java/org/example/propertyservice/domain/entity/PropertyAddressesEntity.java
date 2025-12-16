package org.example.propertyservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.propertyservice.audit.BaseEntity;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "property_addresses")
@Getter
@Setter
@Builder
public class PropertyAddressesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long propertyId;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String streetName;
    private String fullAddress;
    private Double latitude;
    private Double longitude;
}
