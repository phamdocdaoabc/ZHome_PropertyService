package org.example.propertyservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.propertyservice.domain.dto.base.ApiResponse;
import org.example.propertyservice.service.PropertiesService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/property")
public class PropertiesController {
    private final PropertiesService propertiesService;

    @GetMapping("/test")
    public ApiResponse<String> test(){
        return ApiResponse.<String>builder()
                .message("success")
                .traceId(UUID.randomUUID().toString())
                .data("test ok")
                .build();
    }


}
