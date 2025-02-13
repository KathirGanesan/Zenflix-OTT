package com.zenflix.ott.repository;

import com.zenflix.ott.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByName(String name);
    Optional<Genre> findByName(String name);
    List<Genre> findAllByDeletedFalse();
}
