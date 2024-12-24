package com.streamhub.ott.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streamhub.ott.dto.RoleDTO;
import com.streamhub.ott.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can create roles
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return ResponseEntity.status(201)
                .body(createdRole);
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can update roles
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Long roleId,
            @RequestBody RoleDTO roleDTO
    ) {
        RoleDTO updatedRole = roleService.updateRole(roleId, roleDTO);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can delete roles
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Only admins can view all roles
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')") // Only admins can view role by ID
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long roleId) {
        RoleDTO role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }
}

