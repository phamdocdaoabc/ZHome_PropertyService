package org.example.propertyservice.proxy.feign;

import org.example.propertyservice.domain.dto.base.ApiResponse;
import org.example.propertyservice.domain.dto.base.PageResponse;
import org.example.propertyservice.domain.dto.client.UserDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping(value = "/user-service/api/users/test", produces = "application/json")
    ApiResponse<String> test();

    @GetMapping(value = "/user-service/api/users/detail", produces = "application/json")
    ApiResponse<UserDTO> getUser(@RequestParam Long userId);

    @GetMapping(value = "/user-service/api/users", produces = "application/json")
    ApiResponse<PageResponse<UserDTO>> getUsers(@RequestParam Set<Long> ids,
                                                @ParameterObject Pageable pageable);

    @DeleteMapping(value = "/user-service/api/upload/images", produces = "application/json")
    ApiResponse<?> deleteImages(@RequestParam List<String> fileIds);

    @DeleteMapping(value = "/user-service/api/upload/image", produces = "application/json")
    ApiResponse<?> deleteImage(@RequestParam String fileId);
}
