package org.example.propertyservice.proxy.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.propertyservice.domain.dto.client.UserDTO;
import org.example.propertyservice.proxy.UserProxy;
import org.example.propertyservice.proxy.feign.UserClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProxyImpl implements UserProxy {
    private final UserClient userClient;

    @Override
    public String getTest() {
        return userClient.test().getData();
    }

    @Override
    public UserDTO getUser(Long userId) {
        return userClient.getUser(userId).getData();
    }

    @Override
    public Map<Long, UserDTO> getUsersMap(Set<Long> userIds) {
        var response = userClient.getUsers(userIds, Pageable.unpaged()).getData().getContent();
        return response.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));
    }

    @Override
    public void deleteImage(String fileId) {
        userClient.deleteImage(fileId);
    }

    @Override
    public void deleteImages(List<String> fileIds) {
        userClient.deleteImages(fileIds);
    }
}
