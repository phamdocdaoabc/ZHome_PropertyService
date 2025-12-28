package org.example.propertyservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.propertyservice.audit.BaseEntity;
import org.example.propertyservice.domain.enums.MediaType;

@Entity
@Table(name = "property_media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PropertyMediaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    private Long propertyId;
    private String mediaUrl;
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
    private String fileId;
}
