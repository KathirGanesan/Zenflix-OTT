package com.zenflix.ott.service.impl;

import com.zenflix.ott.dto.RegisterRequest;
import com.zenflix.ott.dto.UserDTO;
import com.zenflix.ott.dto.UserResponseDTO;
import com.zenflix.ott.entity.Role;
import com.zenflix.ott.entity.User;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.UserMapper;
import com.zenflix.ott.repository.RoleRepository;
import com.zenflix.ott.repository.UserRepository;
import com.zenflix.ott.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO createUser(UserDTO userDTO) {

        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = userMapper.toEntity(userDTO);

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Fetch roles from the database and set them for the user
        List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("Invalid roles provided. Please check role names.");
        }
        user.setRoles(new HashSet<>(roles)); // Associate roles with the user

        // Save the user with associated roles
        User savedUser = userRepository.save(user);

        // Convert and return the response DTO
        return userMapper.toResponseDTO(savedUser);
    }





    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAllByDeletedFalse().stream()
            .map(userMapper::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserDTO userDTO) {
        // Fetch the existing user
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update basic user details
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setEmail(userDTO.getEmail());

        // If roles are provided in the DTO, update roles
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            List<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
            if (roles.isEmpty()) {
                throw new ResourceNotFoundException("Invalid roles provided. Please check role names.");
            }
            existingUser.setRoles(new HashSet<>(roles));
        }

        // Save updated user
        User updatedUser = userRepository.save(existingUser);

        // Return the updated user as a DTO
        return userMapper.toResponseDTO(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO register(RegisterRequest registerRequest) {
            if (userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
                throw new IllegalArgumentException("Phone number is already in use.");
            }

            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                throw new IllegalArgumentException("Email is already in use.");
            }
            User user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setPhoneNumber(registerRequest.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRoles(new HashSet<>(roleRepository.findByNameIn(Collections.singletonList("ROLE_USER"))));
            user.setDeleted(false);

            User savedUser = userRepository.save(user);
            return userMapper.toResponseDTO(savedUser);
    }

}
