package org.example.propertyservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.propertyservice.config.SecurityUtils;
import org.example.propertyservice.domain.dto.PropertyDTO;
import org.example.propertyservice.domain.dto.PropertyFilterDTO;
import org.example.propertyservice.domain.dto.PropertyPage;
import org.example.propertyservice.domain.dto.base.ApiResponse;
import org.example.propertyservice.domain.dto.base.IdsResponse;
import org.example.propertyservice.domain.dto.base.PageResponse;
import org.example.propertyservice.domain.enums.PropertyStatus;
import org.example.propertyservice.proxy.UserProxy;
import org.example.propertyservice.service.PropertiesService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/property")
public class PropertiesController {
    private final PropertiesService propertiesService;
    private final UserProxy userProxy;

    @GetMapping("/test")
    public ApiResponse<Boolean> test(@RequestParam String role){
        Boolean result = SecurityUtils.hasRole(role);
        return ApiResponse.<Boolean>builder()
                .message("success")
                .traceId(UUID.randomUUID().toString())
                .data(result)
                .build();
    }

    @PostMapping
    public ApiResponse<IdsResponse<Long>> createProperty(@RequestBody PropertyDTO dto){
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Property created successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(propertiesService.create(dto))
                        .build())
                .build();
    }

    @PutMapping
    public ApiResponse<IdsResponse<Long>> updateProperty(@RequestBody PropertyDTO dto){
        return ApiResponse.<IdsResponse<Long>>builder()
                .message("Property updated successfully")
                .traceId(UUID.randomUUID().toString())
                .data(IdsResponse.<Long>builder()
                        .id(propertiesService.update(dto))
                        .build())
                .build();
    }

    @GetMapping("/detail")
    public ApiResponse<PropertyDTO> getProperty(@RequestParam Long id){
        return ApiResponse.<PropertyDTO>builder()
                .message("Property fetched successfully")
                .traceId(UUID.randomUUID().toString())
                .data(propertiesService.getProperty(id))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<PropertyPage>> getPropertyList(@ParameterObject PropertyFilterDTO dto,
                                                                   @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PropertyPage> propertyPages = propertiesService.getList(dto, pageable);
        return ApiResponse.<PageResponse<PropertyPage>>builder()
                .message("Successfully")
                .traceId(UUID.randomUUID().toString()) // chuỗi UUID random
                .data(PageResponse.<PropertyPage>builder()
                        .content(propertyPages.getContent())
                        .page(propertyPages.getNumber())
                        .size(propertyPages.getSize())
                        .sort(pageable.getSort().toString())
                        .totalElements(propertyPages.getTotalElements())
                        .totalPages(propertyPages.getTotalPages())
                        .numberOfElements(propertyPages.getNumberOfElements())
                        .build())
                .build();
    }

    @PutMapping("/status")
    public ApiResponse<Void> updateStatus(@RequestParam Long propertyId,
                                        @RequestParam PropertyStatus status){
        propertiesService.updateStatus(propertyId, status);
        return ApiResponse.<Void>builder()
                .message("Property status updated successfully")
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteProperty(@RequestParam Long id){
        propertiesService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Property deleted successfully")
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    @GetMapping("/suggestions")
    public ApiResponse<List<String>> getSuggestions(@RequestParam String keyword) {
        return ApiResponse.<List<String>>builder()
                .message("Success")
                .data(propertiesService.getSuggestions(keyword))
                .build();
    }
}
