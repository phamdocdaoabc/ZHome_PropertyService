package org.example.propertyservice.domain.dto;

import lombok.*;
import org.example.propertyservice.domain.enums.Direction;
import org.example.propertyservice.domain.enums.LegalStatus;
import org.example.propertyservice.domain.enums.PropertyType;
import org.example.propertyservice.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PropertyFilterDTO {
    // 1. Tìm kiếm chung
    private String search; // Tìm trong title, description, project_name
    private Long userId;
    private Set<Long> ids;

    // 2. Nhóm bắt buộc
    private TransactionType transactionType; // SELL, RENT
    private PropertyType propertyType;    // APARTMENT, HOUSE...

    // 3. Vị trí (Địa lý) propertyAdrressEntity
    private String provinceName;
    private String districtName;
    private String wardName;
    private String streetName;
    // Góc Tây-Nam (Góc dưới bên trái)
    private Double minLat; // hoặc gọi là southLat
    private Double minLng; // hoặc gọi là westLng
    // Góc Đông-Bắc (Góc trên bên phải)
    private Double maxLat; // hoặc gọi là northLat
    private Double maxLng; // hoặc gọi là eastLng

    // 4. Khoảng Giá (User nhập: Tôi tìm nhà từ 2 tỷ đến 5 tỷ)
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private Boolean priceNegotiable; // Có thể thương lượng giá

    // 5. Diện tích
    private Double areaMin;
    private Double areaMax;

    // 6. Chi tiết
    private Direction direction;   // hướng
    private LegalStatus legalStatus; // Sổ đỏ, HĐMB...
    private Boolean isMyPosts;
}
