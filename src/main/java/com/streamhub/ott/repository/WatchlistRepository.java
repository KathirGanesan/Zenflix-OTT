package com.streamhub.ott.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.streamhub.ott.entity.User;
import com.streamhub.ott.entity.Video;
import com.streamhub.ott.entity.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserId(Long userId);
    List<Watchlist> findByVideoId(Long videoId);
    List<Watchlist> findAllByDeletedFalse();
    boolean existsByUserAndVideo(User user, Video video);
}