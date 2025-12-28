package org.example.propertyservice.domain.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.propertyservice.domain.dto.base.ImageUploadResponse;
import org.example.propertyservice.domain.dto.client.UserDTO;
import org.example.propertyservice.domain.enums.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PropertyDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private UserDTO user;
    private String projectName;
    private TransactionType transactionType;
    private PropertyType propertyType;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private Boolean priceNegotiable;
    private Direction direction;
    private Double areaMin;
    private Double areaMax;
    private LegalStatus legalStatus;
    private String title;
    private String description;
    private PropertyStatus status;
    private Long viewCount;
    private Double rating;
    private Integer totalReviews;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long priority;

    private List<ImageUploadResponse> medias;
    private PropertyAddressDTO address;
}
