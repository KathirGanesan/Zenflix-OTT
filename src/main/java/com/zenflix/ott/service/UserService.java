package com.zenflix.ott.service;

import com.zenflix.ott.dto.RegisterRequest;
import com.zenflix.ott.dto.UserDTO;
import com.zenflix.ott.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserDTO userDTO);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);

    UserResponseDTO register(RegisterRequest registerRequest);
}