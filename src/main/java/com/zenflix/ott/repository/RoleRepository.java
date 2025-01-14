package com.zenflix.ott.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zenflix.ott.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findAllByDeletedFalse();
	List<Role> findByNameIn(List<String> names);

    Optional<Object> findByName(String roleUser);
}
