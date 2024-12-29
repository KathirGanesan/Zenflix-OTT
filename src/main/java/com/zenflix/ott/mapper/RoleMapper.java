package com.zenflix.ott.mapper;

import org.springframework.stereotype.Component;

import com.zenflix.ott.dto.RoleDTO;
import com.zenflix.ott.entity.Role;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setDeleted(role.getDeleted()); // Map deleted field
        // Map auditable fields
        roleDTO.setCreatedAt(role.getCreatedAt());
        roleDTO.setCreatedBy(role.getCreatedBy());
        roleDTO.setModifiedAt(role.getModifiedAt());
        roleDTO.setModifiedBy(role.getModifiedBy());
        return roleDTO;
    }

    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        role.setDeleted(roleDTO.getDeleted() != null ? roleDTO.getDeleted() : false); // Handle null for new roles
        return role;
    }
}

