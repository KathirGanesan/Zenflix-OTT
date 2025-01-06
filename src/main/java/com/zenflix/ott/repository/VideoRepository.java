package com.zenflix.ott.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zenflix.ott.entity.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByGenreId(Long genreId);
    List<Video> findAllByDeletedFalse();
    List<Video> findByIsPublicTrailerTrue();
    List<Video> findByGenreIdAndDeletedFalse(Long genreId);
}
