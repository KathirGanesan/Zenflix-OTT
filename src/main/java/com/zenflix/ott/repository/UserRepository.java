package com.zenflix.ott.repository;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zenflix.ott.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByDeletedFalse();
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);}
