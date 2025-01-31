package com.zenflix.ott.repository;

import com.zenflix.ott.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	List<Role> findAllByDeletedFalse();
	List<Role> findByNameIn(List<String> names);
	boolean existsByName(String name);

    Optional<Object> findByName(String roleUser);
}
