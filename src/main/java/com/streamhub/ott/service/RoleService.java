package com.streamhub.ott.service;

import java.util.List;

import com.streamhub.ott.dto.RoleDTO;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(Long roleId, RoleDTO roleDTO);
    void deleteRole(Long roleId);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Long roleId);
}
