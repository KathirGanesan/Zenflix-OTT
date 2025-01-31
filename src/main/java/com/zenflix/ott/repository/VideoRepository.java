package com.zenflix.ott.repository;

import com.zenflix.ott.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    boolean existsByTitleAndIsPublicTrailer(String title, boolean isPublicTrailer);
    List<Video> findByGenreId(Long genreId);
    List<Video> findAllByDeletedFalse();
    List<Video> findByIsPublicTrailerTrue();
    List<Video> findByGenreIdAndDeletedFalse(Long genreId);
}
