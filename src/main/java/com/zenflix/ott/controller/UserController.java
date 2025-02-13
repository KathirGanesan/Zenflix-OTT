package com.zenflix.ott.controller;

import com.zenflix.ott.dto.UserDTO;
import com.zenflix.ott.dto.UserResponseDTO;
import com.zenflix.ott.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')") // Restricts all methods to ADMIN role unless overridden
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can create users
    public ResponseEntity<UserResponseDTO> createUser(@Valid  @RequestBody UserDTO userDTO) {
        UserResponseDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id") // Only admins or the user themselves
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
    	UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can fetch all users
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity
                .ok(users);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Admins and Users can update user details
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
    	UserResponseDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity
                .ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete users
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
