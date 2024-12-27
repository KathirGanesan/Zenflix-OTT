package com.zenflix.ott.service;

import java.util.List;

import com.zenflix.ott.dto.UserDTO;
import com.zenflix.ott.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserDTO userDTO);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
}