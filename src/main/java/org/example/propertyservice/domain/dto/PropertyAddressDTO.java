package org.example.propertyservice.domain.dto;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PropertyAddressDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String provinceName;
    private String districtName;
    private String wardName;
    private String streetName;
    private String fullAddress;
    private Double latitude;
    private Double longitude;
}
