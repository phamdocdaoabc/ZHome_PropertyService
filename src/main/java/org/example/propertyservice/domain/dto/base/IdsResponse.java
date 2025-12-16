package org.example.propertyservice.domain.dto.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdsResponse<T> {
    private T id;
}
