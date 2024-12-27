package com.zenflix.ott.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zenflix.ott.entity.User;
import com.zenflix.ott.entity.Video;
import com.zenflix.ott.entity.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserId(Long userId);
    List<Watchlist> findByVideoId(Long videoId);
    List<Watchlist> findAllByDeletedFalse();
    boolean existsByUserAndVideo(User user, Video video);
}