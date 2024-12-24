package com.streamhub.ott.service;

import java.util.List;

import com.streamhub.ott.dto.UserDTO;
import com.streamhub.ott.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}