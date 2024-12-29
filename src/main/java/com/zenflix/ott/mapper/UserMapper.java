package com.zenflix.ott.mapper;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.UserDTO;
import com.zenflix.ott.dto.UserResponseDTO;
import com.zenflix.ott.entity.User;

@Component
public class UserMapper {

    // Convert User entity to UserDTO
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setDeleted(user.getDeleted());
        userDTO.setPassword(null); // Do not expose the password
        userDTO.setRoles(user.getRoles()
                .stream()
                .map(role -> role.getName()) // Extract role names
                .collect(Collectors.toList())
        );
        // Map auditable fields
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setCreatedBy(user.getCreatedBy());
        userDTO.setModifiedAt(user.getModifiedAt());
        userDTO.setModifiedBy(user.getModifiedBy());
        return userDTO;
    }

    // Convert UserDTO to User entity (excluding roles)
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setDeleted(userDTO.getDeleted());

        // Password should only be set if provided
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }

        return user;
    }
    
    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setDeleted(user.getDeleted());
        userResponseDTO.setRoles(
            user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList())
        );
        // Map auditable fields
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setCreatedBy(user.getCreatedBy());
        userResponseDTO.setModifiedAt(user.getModifiedAt());
        userResponseDTO.setModifiedBy(user.getModifiedBy());
        return userResponseDTO;
    }

}
