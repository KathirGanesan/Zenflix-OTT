package com.streamhub.ott.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streamhub.ott.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findAllByDeletedFalse();
	List<Role> findByNameIn(List<String> names);
}
