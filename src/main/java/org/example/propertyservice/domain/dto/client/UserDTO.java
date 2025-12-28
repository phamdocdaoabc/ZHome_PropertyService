package org.example.propertyservice.domain.dto.client;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String fullName;
    private String imageUrl;
    private String email;
    private String phone;
    private String bio;
}
