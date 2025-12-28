package org.example.propertyservice.proxy;

import org.example.propertyservice.domain.dto.client.UserDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserProxy {
    String getTest();

    UserDTO getUser(Long userId);

    Map<Long, UserDTO> getUsersMap(Set<Long> userIds);

    void deleteImage(String fileId);

    void deleteImages(List<String> fileIds);
}
