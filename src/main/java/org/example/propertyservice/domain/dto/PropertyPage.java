package org.example.propertyservice.domain.dto;
import lombok.*;
import org.example.propertyservice.domain.dto.client.UserDTO;
import org.example.propertyservice.domain.enums.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PropertyPage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private UserDTO user;
    private String projectName;
    private TransactionType transactionType;
    private PropertyType propertyType;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private Boolean priceNegotiable;
    private List<String> mediaUrls;
    private PropertyAddressDTO address;
}
