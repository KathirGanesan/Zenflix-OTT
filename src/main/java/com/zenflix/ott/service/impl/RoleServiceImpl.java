package com.zenflix.ott.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zenflix.ott.dto.RoleDTO;
import com.zenflix.ott.entity.Role;
import com.zenflix.ott.exception.ResourceNotFoundException;
import com.zenflix.ott.mapper.RoleMapper;
import com.zenflix.ott.repository.RoleRepository;
import com.zenflix.ott.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final RoleMapper roleMapper;

	public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
		this.roleRepository = roleRepository;
		this.roleMapper = roleMapper;
	}

	@Override
	public RoleDTO createRole(RoleDTO roleDTO) {
		Role role = roleMapper.toEntity(roleDTO);
		Role savedRole = roleRepository.save(role);
		return roleMapper.toDTO(savedRole);
	}

	@Override
	public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
		role.setName(roleDTO.getName());
		Role updatedRole = roleRepository.save(role);
		return roleMapper.toDTO(updatedRole);
	}

	@Override
	public void deleteRole(Long roleId) {
	    Role role = roleRepository.findById(roleId)
	            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
	    role.setDeleted(true);
	    roleRepository.save(role);
	}


	@Override
	public List<RoleDTO> getAllRoles() {
		return roleRepository.findAllByDeletedFalse().stream().map(roleMapper::toDTO).collect(Collectors.toList());
	}

	@Override
	public RoleDTO getRoleById(Long roleId) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
		return roleMapper.toDTO(role);
	}
}
